package Entities;

// Define the message class if needed
public class UserStatusMessage {
    private String type;        // "userJoined", "userLeft", or "userAFK"
    private String userId;      // User's ID
    private String username;    // User's display name
    private String timestamp;   // ISO timestamp


    // Constructor with all fields (optional, but good practice)
    public UserStatusMessage(String type, String userId, String username, String timestamp) {
        this.type = type;
        this.userId = userId;
        this.username = username;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}