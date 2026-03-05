package com.uims.dao;

import com.uims.db.DBConnection;
import com.uims.model.User;

import java.sql.*;

/**
 * UserDAO – Data Access Object for the <code>Users</code> table.
 *
 * Responsibilities:
 *  - Validate user credentials at login (authenticate)
 *  - Retrieve a single user by username or ID
 *
 * All methods open a fresh connection, perform their work, and close the
 * connection in a finally block to prevent connection leaks.
 */
public class UserDAO {

    /**
     * Validates the supplied credentials against the database.
     *
     * @param username plain-text username
     * @param password plain-text password (see note below)
     * @return a populated {@link User} object on success, or {@code null} if
     *         credentials do not match any record
     *
     * NOTE: For a production system you should hash the password (e.g. BCrypt)
     * before storing and compare hashes here rather than plain text.
     */
    public User authenticate(String username, String password) {
        String sql = "SELECT user_id, username, full_name, role "
                   + "FROM Users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // replace with a proper logger in production
        }
        return null;
    }

    /**
     * Fetches a single user by primary key.
     *
     * @param userId the primary key to look up
     * @return the matching {@link User}, or {@code null} if not found
     */
    public User getUserById(int userId) {
        String sql = "SELECT user_id, username, full_name, role, created_at "
                   + "FROM Users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
