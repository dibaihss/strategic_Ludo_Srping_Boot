package Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Relationship with User entity - one session can have up to 4 users
    @ManyToMany
    @JoinTable(
        name = "session_users",
        joinColumns = @JoinColumn(name = "session_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>(4); // Initialize with capacity of 4
    
    // Additional game-related fields
    private Integer maxPlayers = 4;
    private Integer currentPlayers = 0;
    
    // Constructor
    public Session() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Add a user to the session if there's room
    public boolean addUser(User user) {
        if (users.size() < maxPlayers) {
            users.add(user);
            currentPlayers = users.size();
            return true;
        }
        return false; // Session is full
    }
    
    // Remove a user from the session
    public boolean removeUser(User user) {
        boolean removed = users.remove(user);
        if (removed) {
            currentPlayers = users.size();
        }
        return removed;
    }
    
    // Check if the session is full
    public boolean isFull() {
        return users.size() >= maxPlayers;
    }
    
    // Getters and Setters
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
        this.currentPlayers = users.size();
    }
    
    public Integer getMaxPlayers() {
        return maxPlayers;
    }
    
    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
    
    public Integer getCurrentPlayers() {
        return currentPlayers;
    }
    
    public void setCurrentPlayers(Integer currentPlayers) {
        this.currentPlayers = currentPlayers;
    }
    
    // Update the updatedAt timestamp before saving
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}