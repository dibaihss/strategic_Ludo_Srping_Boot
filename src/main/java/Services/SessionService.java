package Services;

import Entities.Session;
import Entities.User;
import Interfaces.SessionRepo;
import Interfaces.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private UserRepo userRepo;

    // Get all sessions
    public List<Session> getAllSessions() {
        return sessionRepo.findAll();
    }

    // Get session by ID
    public Optional<Session> getSessionById(Long id) {
        return sessionRepo.findById(id);
    }

    // Create a new session
    public Session createSession(Session session) {
        return sessionRepo.save(session);
    }

    // Update a session
    public Session updateSession(Session session) {
        return sessionRepo.save(session);
    }

    // Delete a session
    public void deleteSession(Long id) {
        sessionRepo.deleteById(id);
    }

    // Find sessions by status
    public List<Session> getSessionsByStatus(String status) {
        return sessionRepo.findByStatus(status);
    }

    // Find available sessions (not full)
    public List<Session> getAvailableSessions() {
        return sessionRepo.findByCurrentPlayersLessThan(4);
    }

    // Add a user to a session
    public boolean addUserToSession(Long sessionId, Long userId) {
        Optional<Session> sessionOpt = sessionRepo.findById(sessionId);
        Optional<User> userOpt = userRepo.findById(userId);

        if (sessionOpt.isPresent() && userOpt.isPresent()) {
            Session session = sessionOpt.get();
            User user = userOpt.get();

            // Check if session is full
            if (session.isFull()) {
                return false;
            }
            
            // Check if user is already in the session
            if (session.getUsers().contains(user)) {
                return true; // User is already in this session
            }
            // Add user to session
            boolean added = session.addUser(user);
            if (added) {
                if (session.isFull()) {
                    session.setStatus("full");
                }
                sessionRepo.save(session);
                return true;
            }
        }
        return false;
    }

    // Remove a user from a session
    public boolean removeUserFromSession(Long sessionId, Long userId) {
        Optional<Session> sessionOpt = sessionRepo.findById(sessionId);
        Optional<User> userOpt = userRepo.findById(userId);

        if (sessionOpt.isPresent() && userOpt.isPresent()) {
            Session session = sessionOpt.get();
            User user = userOpt.get();

            boolean removed = session.removeUser(user);
            if (removed) {
                sessionRepo.save(session);
                return true;
            }
        }
        return false;
    }

    // Get all users in a session
    public List<User> getUsersInSession(Long sessionId) {
        Optional<Session> sessionOpt = sessionRepo.findById(sessionId);
        return sessionOpt.map(Session::getUsers).orElse(null);
    }

    public void deleteEmptySessions() {
        List<Session> emptySessions = sessionRepo.findByCurrentPlayers(0);
        if (!emptySessions.isEmpty()) {
            sessionRepo.deleteAll(emptySessions);
        }
    }
}