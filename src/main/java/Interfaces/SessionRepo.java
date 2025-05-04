package Interfaces;

import Entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface SessionRepo extends JpaRepository<Session, Long> {
    // Find sessions by status
    List<Session> findByStatus(String status);

    // Find sessions that aren't full
    List<Session> findByCurrentPlayersLessThan(Integer playerCount);

    // Find sessions by name containing keyword
    List<Session> findByNameContaining(String keyword);

    List<Session> findByCurrentPlayers(int currentPlayers);

    @Query("SELECT s FROM Session s JOIN s.users u WHERE u.id = :userId")
    Optional<Session> findSessionByUserId(@Param("userId") Long userId);
}