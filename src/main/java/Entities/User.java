package Entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean status;
    private boolean isGuest = false;
    @Column(unique = true) // Make email unique
    private String email; // Added email attribute
    private String password; // Added password a

    // Add the many-to-many relationship with Session
    // JsonIgnore prevents infinite recursion during JSON serialization
    @JsonIgnore
    @ManyToMany(mappedBy = "users", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Session> sessions = new HashSet<>();

    // Helper method to clean up associations before deletion
    @PreRemove
    private void removeUserFromSessions() {
        for (Session session : new HashSet<>(sessions)) {
            session.getUsers().remove(this);
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getEmail() { // Added getter for email
        return email;
    }

    public void setEmail(String email) { // Added setter for email
        this.email = email;
    }

    public String getPassword() { // Added getter for password
        return password;
    }

    public void setPassword(String password) { // Added setter for password
        this.password = password;
    }

    // Add getters and setters for the sessions relationship
    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }

    // Helper method to add a session
    public void addSession(Session session) {
        this.sessions.add(session);
    }

    // Helper method to remove a session
    public void removeSession(Session session) {
        this.sessions.remove(session);
    }

    // Add getter and setter
    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    @Column
    private LocalDateTime lastActivity = LocalDateTime.now();

    // Add getter and setter for lastActivity
    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Getter for createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}