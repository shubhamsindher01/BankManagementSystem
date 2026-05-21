
package gui;

import model.*;
import service.AccountService;
import utils.BankException;

import java.util.*;

public class ConsoleApp {

  private final AccountService service = new AccountService();
  private final Scanner scanner = new Scanner(System.in);

  public void run() {
    printBanner();
    boolean running = true;
    while (running) {
      printMenu();
      int choice = readInt("Enter your choice: ");
      switch (choice) {
        case 1 -> createAccount();
        case 2 -> deposit();
        case 3 -> withdraw();
        case 4 -> checkBalance();
        case 5 -> viewAccountDetails();
        case 6 -> transactionHistory();
        case 7 -> deleteAccount();
        case 8 -> {
          System.out.println("\n  Thank you for banking with us! Goodbye!\n");
          running = false;
        }
        default -> System.out.println("  Invalid choice. Please try again.");
      }
    }
    scanner.close();
  }

  private void createAccount() {
    printSection("CREATE NEW ACCOUNT");
    System.out.print("  Full Name      : ");
    String name = scanner.nextLine();
    System.out.print("  Email          : ");
    String email = scanner.nextLine();
    System.out.print("  Phone          : ");
    String phone = scanner.nextLine();
    System.out.print("  Address        : ");
    String address = scanner.nextLine();
    System.out.print("  Account Type (1=Savings / 2=Current): ");
    int typeChoice = readInt("");
    Account.AccountType type = (typeChoice == 2) ? Account.AccountType.CURRENT : Account.AccountType.SAVINGS;
    double deposit = readDouble("  Initial Deposit: ");
    System.out.print("  Set 4-digit PIN: ");
    String pin = scanner.nextLine();

    try {
      Customer cust = new Customer(name, email, phone, address);
      Account acc = service.createAccount(cust, type, deposit, pin);
      printSuccess("Account created successfully!");
      System.out.println("  Account Number : " + acc.getAccountNumber());
      System.out.println("  Account Type   : " + acc.getAccountType());
      System.out.printf("  Balance        : Rs.%.2f%n", acc.getBalance());
      System.out.println("  Please save your account number and PIN!\n");
    } catch (BankException e) {
      printError(e.getMessage());
    }
  }

  private void deposit() {
    printSection("DEPOSIT MONEY");
    System.out.print("  Account Number : ");
    String accNo = scanner.nextLine();
    double amount = readDouble("  Deposit Amount : ");
    try {
      double newBal = service.deposit(accNo, amount);
      printSuccess("Deposit successful!");
      System.out.printf("  New Balance: Rs.%.2f%n%n", newBal);
    } catch (BankException e) {
      printError(e.getMessage());
    }
  }

  private void withdraw() {
    printSection("WITHDRAW MONEY");
    System.out.print("  Account Number : ");
    String accNo = scanner.nextLine();
    System.out.print("  PIN            : ");
    String pin = scanner.nextLine();
    double amount = readDouble("  Withdraw Amount: ");
    try {
      double newBal = service.withdraw(accNo, pin, amount);
      printSuccess("Withdrawal successful!");
      System.out.printf("  Remaining Balance: Rs.%.2f%n%n", newBal);
    } catch (BankException e) {
      printError(e.getMessage());
    }
  }

  private void checkBalance() {
    printSection("CHECK BALANCE");
    System.out.print("  Account Number : ");
    String accNo = scanner.nextLine();
    System.out.print("  PIN            : ");
    String pin = scanner.nextLine();
    try {
      double bal = service.checkBalance(accNo, pin);
      printSuccess("Balance retrieved.");
      System.out.printf("  Available Balance: Rs.%.2f%n%n", bal);
    } catch (BankException e) {
      printError(e.getMessage());
    }
  }

  private void viewAccountDetails() {
    printSection("ACCOUNT DETAILS");
    System.out.print("  Account Number : ");
    String accNo = scanner.nextLine();
    try {
      String[] d = service.getAccountDetails(accNo);
      System.out.println("\n  ================================");
      System.out.println("       ACCOUNT INFORMATION");
      System.out.println("  ================================");
      System.out.println("  Account No   : " + d[0]);
      System.out.println("  Type         : " + d[1]);
      System.out.println("  Balance      : Rs." + d[2]);
      System.out.println("  Status       : " + d[3]);
      System.out.println("  Opened On    : " + d[4]);
      System.out.println("  --------------------------------");
      System.out.println("  Name         : " + d[5]);
      System.out.println("  Email        : " + d[6]);
      System.out.println("  Phone        : " + d[7]);
      System.out.println("  Address      : " + d[8]);
      System.out.println("  ================================\n");
    } catch (BankException e) {
      printError(e.getMessage());
    }
  }

  private void transactionHistory() {
    printSection("TRANSACTION HISTORY");
    System.out.print("  Account Number : ");
    String accNo = scanner.nextLine();
    try {
      var txns = service.getTransactionHistory(accNo);
      if (txns.isEmpty()) {
        System.out.println("  No transactions found.\n");
        return;
      }
      System.out.println("\n  Date                   Type         Amount       Balance      Description");
      System.out.println("  " + "-".repeat(85));
      for (var t : txns) {
        System.out.printf("  %-22s %-12s %12.2f %12.2f  %s%n",
            t.getFormattedDate(), t.getTransactionType(),
            t.getAmount(), t.getBalanceAfter(), t.getDescription());
      }
      System.out.println();
    } catch (BankException e) {
      printError(e.getMessage());
    }
  }

  private void deleteAccount() {
    printSection("DELETE ACCOUNT");
    System.out.print("  Account Number : ");
    String accNo = scanner.nextLine();
    System.out.print("  PIN            : ");
    String pin = scanner.nextLine();
    System.out.print("  Are you sure? (yes/no): ");
    String confirm = scanner.nextLine();
    if (!confirm.equalsIgnoreCase("yes")) {
      System.out.println("  Cancelled.\n");
      return;
    }
    try {
      service.deleteAccount(accNo, pin);
      printSuccess("Account deactivated successfully.\n");
    } catch (BankException e) {
      printError(e.getMessage());
    }
  }

  private void printBanner() {
    System.out.println("\n  ====================================");
    System.out.println("     BANK MANAGEMENT SYSTEM v1.0");
    System.out.println("  ====================================\n");
  }

  private void printMenu() {
    System.out.println("  ================================");
    System.out.println("           MAIN MENU");
    System.out.println("  ================================");
    System.out.println("  1. Create Account");
    System.out.println("  2. Deposit Money");
    System.out.println("  3. Withdraw Money");
    System.out.println("  4. Check Balance");
    System.out.println("  5. View Account Details");
    System.out.println("  6. Transaction History");
    System.out.println("  7. Delete Account");
    System.out.println("  8. Exit");
    System.out.println("  ================================");
  }

  private void printSection(String title) {
    System.out.println("\n  -- " + title + " --");
  }

  private void printSuccess(String msg) {
    System.out.println("  OK: " + msg);
  }

  private void printError(String msg) {
    System.out.println("  ERROR: " + msg + "\n");
  }

  private int readInt(String prompt) {
    System.out.print(prompt);
    try {
      return Integer.parseInt(scanner.nextLine().trim());
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  private double readDouble(String prompt) {
    System.out.print(prompt);
    try {
      return Double.parseDouble(scanner.nextLine().trim());
    } catch (NumberFormatException e) {
      return -1;
    }
  }
}