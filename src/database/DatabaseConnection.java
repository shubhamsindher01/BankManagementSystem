package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;

    private DatabaseConnection() {
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {

            // Load SQLite JDBC Driver
            Class.forName("org.sqlite.JDBC");

            // Create connection
            Connection conn = DriverManager.getConnection(
                    "jdbc:sqlite:bank_management.db"
            );

            return conn;

        } catch (ClassNotFoundException e) {

            System.err.println("[DB ERROR] Driver not found: " + e.getMessage());
            return null;

        } catch (SQLException e) {

            System.err.println("[DB ERROR] Connection failed: " + e.getMessage());
            return null;
        }
    }
}