package Interfaces;

import Entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    // Spring Data JPA automatically implements basic CRUD operations

    // Custom query methods
    List<User> findByStatus(boolean status);

    // Custom query using JPQL
    @Query("SELECT g FROM User g WHERE g.name LIKE %:keyword%")
    List<User> searchByNameContaining(@Param("keyword") String keyword);

    Optional<User> findByEmail(String email);

    List<User> findByIsGuestTrueAndCreatedAtBefore(LocalDateTime cutoffTime);

    List<User> findByIsGuestTrue();
}