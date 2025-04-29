package Services;

import Interfaces.UserRepo;
import Entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public void deleteInactiveGuestUsers(int hoursOld) {
        List<User> guestUsers = userRepo.findByIsGuestTrue();

        // Get current time minus the specified hours
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(hoursOld);

        // Filter guest users created more than hoursOld ago
        List<User> oldGuestUsers = guestUsers.stream()
                .filter(user -> user.getCreatedAt() != null && user.getCreatedAt().isBefore(cutoffTime))
                .collect(Collectors.toList());

        if (!oldGuestUsers.isEmpty()) {
            userRepo.deleteAll(oldGuestUsers);
        }
    }
}