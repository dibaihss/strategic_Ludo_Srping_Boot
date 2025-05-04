package Controllers;

import Entities.Session;
import Entities.StatusUpdateRequest;
import Entities.User;
import Services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
public class SessionsController {

    @Autowired
    private SessionService sessionService;

    // Get all sessions
    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    // Get a session by ID
    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        Optional<Session> session = sessionService.getSessionById(id);
        return session.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new session
    @PostMapping
    public ResponseEntity<Session> createSession(@RequestBody Session session) {
        Session createdSession = sessionService.createSession(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }

    // Update a session
    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable Long id, @RequestBody Session session) {
        Optional<Session> existingSession = sessionService.getSessionById(id);
        if (!existingSession.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        session.setId(id);
        Session updatedSession = sessionService.updateSession(session);
        return ResponseEntity.ok(updatedSession);
    }

    // Delete a session
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        Optional<Session> existingSession = sessionService.getSessionById(id);
        if (!existingSession.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    // Get available sessions (not full)
    @GetMapping("/available")
    public ResponseEntity<List<Session>> getAvailableSessions() {
        List<Session> sessions = sessionService.getAvailableSessions();
        return ResponseEntity.ok(sessions);
    }

    // Get sessions by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Session>> getSessionsByStatus(@PathVariable String status) {
        List<Session> sessions = sessionService.getSessionsByStatus(status);
        return ResponseEntity.ok(sessions);
    }

    // Add a user to a session
    @PostMapping("/{sessionId}/users/{userId}")
    public ResponseEntity<Map<String, Object>> addUserToSession(
            @PathVariable Long sessionId, 
            @PathVariable Long userId) {
                
          // Remove the user from any other session
        sessionService.removeUserFromOtherSessions(userId);

        boolean added = sessionService.addUserToSession(sessionId, userId);
        
        Map<String, Object> response = new HashMap<>();
        if (added) {
            response.put("success", true);
            response.put("message", "User added to session successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to add user to session. Session might be full or user/session might not exist");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Remove a user from a session
    @DeleteMapping("/{sessionId}/users/{userId}")
    public ResponseEntity<Map<String, Object>> removeUserFromSession(
            @PathVariable Long sessionId, 
            @PathVariable Long userId) {
        
        boolean removed = sessionService.removeUserFromSession(sessionId, userId);
        
        Map<String, Object> response = new HashMap<>();
        if (removed) {
            response.put("success", true);
            response.put("message", "User removed from session successfully");
            // Optionally delete empty sessions after removing a user
            sessionService.deleteEmptySessions();
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to remove user from session. User might not be in the session or session might not exist");
            return ResponseEntity.badRequest().body(response);
        }
       
    }

    // Get all users in a session
    @GetMapping("/{sessionId}/users")
    public ResponseEntity<List<User>> getUsersInSession(@PathVariable Long sessionId) {
        List<User> users = sessionService.getUsersInSession(sessionId);
        if (users == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }
}