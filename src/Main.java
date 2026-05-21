import gui.BankGUI;
import gui.ConsoleApp;

import javax.swing.*;

/**
 * Main - Entry point of the Bank Management System
 *
 * Run modes:
 * java Main → Swing GUI mode
 * java Main console → Console mode
 *
 * OOP Concepts demonstrated in this project:
 * ✅ Encapsulation – All model classes use private fields + getters/setters
 * ✅ Inheritance – SavingsAccount & CurrentAccount extend Account
 * ✅ Polymorphism – getInterestRate() overridden in subclasses
 * ✅ Abstraction – Service layer hides DB logic from GUI
 * ✅ Exception Handling – Custom BankException with error codes
 * ✅ Design Patterns – Singleton (DatabaseConnection)
 * ✅ JDBC – Full MySQL integration
 * ✅ Swing GUI – Professional graphical interface
 * ✅ Security – SHA-256 PIN hashing
 */
public class Main {

  public static void main(String[] args) {
    boolean consoleMode = args.length > 0 && args[0].equalsIgnoreCase("console");

    if (consoleMode) {
      // ── Console Mode ─────────────────────────
      System.out.println("Starting in Console Mode...");
      new ConsoleApp().run();
    } else {
      // ── GUI Mode ─────────────────────────────
      SwingUtilities.invokeLater(() -> {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        new BankGUI();
      });
    }
  }
}