package com.uims.model;

import java.sql.Timestamp;

/**
 * User – represents a system user (Administrator or Storekeeper).
 *
 * Corresponds to the <code>Users</code> table in the database.
 */
public class User {

    private int       userId;
    private String    username;
    private String    password;   // never expose in JSP; used only during authentication
    private String    fullName;
    private String    role;       // "Administrator" or "Storekeeper"
    private Timestamp createdAt;

    /** Default constructor required by some frameworks. */
    public User() { }

    public User(int userId, String username, String fullName, String role) {
        this.userId   = userId;
        this.username = username;
        this.fullName = fullName;
        this.role     = role;
    }

    // Getters and setters

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    /** Convenience check – true when the user is an Administrator. */
    public boolean isAdmin() {
        return "Administrator".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", username='" + username
               + "', role='" + role + "'}";
    }
}
