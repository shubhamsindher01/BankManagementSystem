package utils;

import database.DatabaseConnection;
import java.sql.*;

/**
 * AccountNumberGenerator - Auto-generates unique account numbers
 */
public class AccountNumberGenerator {

  private AccountNumberGenerator() {
  }

  public static String generate() {
    String prefix = "ACC";
    int nextId = 1;

    String sql = "SELECT COUNT(*) FROM accounts";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      if (rs.next()) {
        nextId = rs.getInt(1) + 1;
      }
    } catch (SQLException e) {
      System.err.println("Error generating account number: " + e.getMessage());
    }

    return String.format("%s%07d", prefix, nextId);
  }
}