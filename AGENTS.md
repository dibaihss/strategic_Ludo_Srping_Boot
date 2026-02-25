# AGENTS.md - Strategic Ludo Game Backend

## Project Overview

This is a Spring Boot 3.4.5 backend application for a Strategic Ludo game. It provides REST APIs and WebSocket communication for managing users and game sessions.

- **Java Version**: 21
- **Build Tool**: Maven
- **Database**: PostgreSQL (Azure)
- **Key Frameworks**: Spring Boot, Spring Security, Spring Data JPA, WebSocket

---

## Build Commands

### Compile and Package
```bash
mvn clean package        # Build and package the application
mvn clean install        # Build, test, and install to local repository
mvn spring-boot:run      # Run the application directly
```

### Running Tests
```bash
mvn test                 # Run all tests
mvn test -Dtest=ClassName             # Run a specific test class
mvn test -Dtest=ClassName#methodName  # Run a specific test method
mvn test -Dtest="*Controller*"        # Run tests matching pattern
mvn verify               # Run tests and verify
```

### Code Quality
```bash
mvn compile              # Compile without tests
mvn clean                # Clean build artifacts
```

---

## Code Style Guidelines

### Package Structure
- **Controllers** - REST API endpoints
- **Services** - Business logic
- **Entities** - JPA entities / DTOs
- **Interfaces** - Repository interfaces
- **config** - Configuration classes

Example:
```java
package Controllers;
package Services;
package Entities;
package Interfaces;
package com.strategic.ludo.strategicLudo.config;
```

### Naming Conventions
- **Classes**: PascalCase (e.g., `UsersController`, `UserService`)
- **Methods**: camelCase (e.g., `getAllUsers()`, `saveUser()`)
- **Variables**: camelCase (e.g., `userService`, `clientIp`)
- **Packages**: PascalCase (e.g., `Controllers`, `Services`)
- **Constants**: UPPER_SNAKE_CASE

### Import Organization
Order imports by:
1. Java/Jakarta standard library
2. Spring framework imports
3. Third-party libraries
4. Project internal imports

```java
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Entities.User;
import Services.UserService;
```

### REST Controller Patterns
- Use `@RestController` and `@RequestMapping("/api")`
- Return `ResponseEntity<T>` for all endpoints
- Use appropriate HTTP status codes (200, 201, 400, 404, 500)
- Use `@PathVariable` for path parameters and `@RequestBody` for request bodies
- Use `@RequestParam` for query parameters

```java
@GetMapping("/users/{id}")
public ResponseEntity<User> getUserById(@PathVariable Long id) {
    Optional<User> user = userService.getUserById(id);
    return user.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

### Entity Guidelines
- Use JPA annotations from `jakarta.persistence`
- Use `@JsonIgnore` to prevent circular references
- Use `@CreationTimestamp` for automatic timestamp handling
- Use helper methods for relationships

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private Set<Session> sessions = new HashSet<>();
}
```

### Dependency Injection
- Use `@Autowired` for field injection
- Use `@Service` for service classes
- Use `@Repository` for repository interfaces

```java
@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
}
```

### Security
- Always encode passwords using `PasswordEncoder`
- Use BCrypt password hashing
- Return appropriate error messages for authentication failures

```java
user.setPassword(passwordEncoder.encode(user.getPassword()));
if (passwordEncoder.matches(rawPassword, encodedPassword)) { ... }
```

### Error Handling
- Return meaningful HTTP status codes
- Use `ResponseEntity<?>` for endpoints that may return errors
- Handle Optional with `.map()` or `.orElse()`

```java
return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body("Invalid email or password");
return ResponseEntity.notFound().build();
```

### WebSocket Configuration
- Use `@MessageMapping` for WebSocket endpoints
- Use `@SendTo` or `SimpMessagingTemplate` for responses

---

## Testing Guidelines

### Test Structure
- Place tests in `src/test/java` matching the main package structure
- Use `@SpringBootTest` for integration tests
- Use `@ActiveProfiles("test")` for test-specific configuration
- Use JUnit 5 (`org.junit.jupiter.api.Test`)

```java
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    @Test
    void testMethod() { ... }
}
```

---

## Configuration

### Environment Variables
- `PGUSER` - PostgreSQL username
- `PGPASSWORD` - PostgreSQL password
- `PORT` - Server port (default: 8080)

### Application Properties
Database configuration is in `src/main/resources/application.properties`.

---

## Common Patterns

### Rate Limiting
Use `RateLimiterService` to prevent abuse:
```java
if (rateLimiter.isRateLimited(clientIp)) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .body("Too many requests. Please try again later.");
}
```

### Getting Client IP
```java
private String getClientIp(HttpServletRequest request) {
    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
    if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
        return request.getRemoteAddr();
    }
    return xForwardedForHeader.split(",")[0].trim();
}
```
