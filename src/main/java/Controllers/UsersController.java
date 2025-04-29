package Controllers;

import Services.RateLimiterService;
import Services.UserService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import Entities.LoginRequest;
import Entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    private UserService userService;
    // Inject PasswordEncoder

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RateLimiterService rateLimiter;

    private String getClientIp(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xForwardedForHeader.split(",")[0].trim();
    }

    // New endpoints to interact with database
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Encode the password before saving

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/guest-login")
    public ResponseEntity<?> guestLogin(HttpServletRequest request) {
        // Get client IP
        String clientIp = getClientIp(request);

        if (rateLimiter.isRateLimited(clientIp)) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many guest accounts created. Please try again later.");
        }

        // Create a temporary user with guest status
        User guestUser = new User();
        guestUser.setName("Guest_" + UUID.randomUUID().toString().substring(0, 8));
        guestUser.setStatus("active");
        guestUser.setGuest(true);

        // Set the creation timestamp manually (though this will be handled by
        // @CreationTimestamp)
        guestUser.setCreatedAt(LocalDateTime.now());

        // Save the guest user to the database (optional)
        // If you save, you may want to set up a scheduled task to clean up old guest
        // accounts
        User savedUser = userService.saveUser(guestUser);

        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Find user by email
        Optional<User> userOptional = userService.findByEmail(loginRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Compare the raw password with the hashed one in the database
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // Password matches
                return ResponseEntity.ok(user); // Return user info or a token
            }
        }

        // Authentication failed
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
}