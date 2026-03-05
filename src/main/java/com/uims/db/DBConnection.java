package com.uims.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection – central database connection helper.
 *
 * Usage:
 *   Connection conn = DBConnection.getConnection();
 *
 * Configuration:
 *   Edit the three constants below to match your MySQL installation:
 *     DB_URL      – JDBC URL including the database name
 *     DB_USERNAME – MySQL user that has rights to the schema
 *     DB_PASSWORD – password for that MySQL user
 *
 * The class follows the Singleton-style factory pattern: every caller
 * gets a fresh {@link Connection} from the same driver, which is
 * sufficient for a class-project workload.  For production systems
 * consider replacing this with a JNDI/DataSource connection pool.
 */
public class DBConnection {

    // ---------------------------------------------------------------
    // Database configuration – update these values for your setup
    // ---------------------------------------------------------------
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/university_inventory"
                                            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "your_password_here"; // change before running

    // Load the JDBC driver once when the class is first referenced
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                "MySQL JDBC Driver not found. Add mysql-connector-java to your classpath.\n" + e);
        }
    }

    /** Private constructor – this is a utility class; do not instantiate. */
    private DBConnection() { }

    /**
     * Opens and returns a new {@link Connection} to the configured database.
     *
     * @return a live {@link Connection} (caller is responsible for closing it)
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
}
