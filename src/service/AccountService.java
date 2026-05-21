
package service;

import database.DatabaseConnection;
import model.*;
import utils.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

  private Connection getConn() throws SQLException {
    Connection conn = DatabaseConnection.getInstance().getConnection();
    if (conn == null)
      throw new SQLException("Could not connect to database.");
    return conn;
  }

  public Account createAccount(Customer customer, Account.AccountType type,
      double initialDeposit, String pin) throws BankException {
    if (initialDeposit < 500)
      throw new BankException("MIN_DEPOSIT", "Minimum initial deposit is Rs.500.");
    if (pin == null || pin.length() != 4 || !pin.matches("\\d{4}"))
      throw new BankException("INVALID_PIN_FORMAT", "PIN must be exactly 4 digits.");

    try (Connection conn = getConn()) {
      conn.setAutoCommit(false);

      String custSql = "INSERT INTO customers (full_name, email, phone, address) VALUES (?,?,?,?)";
      int customerId;
      try (PreparedStatement ps = conn.prepareStatement(custSql, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, customer.getFullName());
        ps.setString(2, customer.getEmail());
        ps.setString(3, customer.getPhone());
        ps.setString(4, customer.getAddress());
        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        if (!keys.next())
          throw new BankException("Failed to create customer record.");
        customerId = keys.getInt(1);
      }

      String accNo = AccountNumberGenerator.generate();
      String hashedPin = HashUtil.sha256(pin);

      String accSql = "INSERT INTO accounts (account_number, customer_id, account_type, balance, pin) VALUES (?,?,?,?,?)";
      try (PreparedStatement ps = conn.prepareStatement(accSql)) {
        ps.setString(1, accNo);
        ps.setInt(2, customerId);
        ps.setString(3, type.name());
        ps.setDouble(4, initialDeposit);
        ps.setString(5, hashedPin);
        ps.executeUpdate();
      }

      String logSql = "INSERT INTO transactions (account_number, transaction_type, amount, balance_after, description) VALUES (?,?,?,?,?)";
      try (PreparedStatement ps = conn.prepareStatement(logSql)) {
        ps.setString(1, accNo);
        ps.setString(2, "DEPOSIT");
        ps.setDouble(3, initialDeposit);
        ps.setDouble(4, initialDeposit);
        ps.setString(5, "Initial Deposit");
        ps.executeUpdate();
      }

      conn.commit();

      if (type == Account.AccountType.SAVINGS)
        return new SavingsAccount(accNo, customerId, initialDeposit, hashedPin);
      else
        return new CurrentAccount(accNo, customerId, initialDeposit, hashedPin);

    } catch (SQLIntegrityConstraintViolationException e) {
      throw BankException.duplicateEmail(customer.getEmail());
    } catch (SQLException e) {
      throw new BankException("DB_ERROR", "Database error: " + e.getMessage());
    }
  }

  public double deposit(String accountNumber, double amount) throws BankException {
    validateAmount(amount);
    Account acc = getAccountOrThrow(accountNumber);
    double newBalance = acc.getBalance() + amount;
    try (Connection conn = getConn()) {
      updateBalance(conn, accountNumber, newBalance);
      logTransaction(conn, accountNumber, "DEPOSIT", amount, newBalance, "Cash Deposit");
    } catch (SQLException e) {
      throw new BankException("DB_ERROR", e.getMessage());
    }
    return newBalance;
  }

  public double withdraw(String accountNumber, String pin, double amount) throws BankException {
    validateAmount(amount);
    Account acc = getAccountOrThrow(accountNumber);
    if (!HashUtil.verifyPin(pin, acc.getPin()))
      throw BankException.invalidPin();
    if (acc.getBalance() < amount)
      throw BankException.insufficientFunds();
    double newBalance = acc.getBalance() - amount;
    try (Connection conn = getConn()) {
      updateBalance(conn, accountNumber, newBalance);
      logTransaction(conn, accountNumber, "WITHDRAWAL", amount, newBalance, "Cash Withdrawal");
    } catch (SQLException e) {
      throw new BankException("DB_ERROR", e.getMessage());
    }
    return newBalance;
  }

  public double checkBalance(String accountNumber, String pin) throws BankException {
    Account acc = getAccountOrThrow(accountNumber);
    if (!HashUtil.verifyPin(pin, acc.getPin()))
      throw BankException.invalidPin();
    return acc.getBalance();
  }

  public String[] getAccountDetails(String accountNumber) throws BankException {
    String sql = "SELECT a.account_number, a.account_type, a.balance, a.is_active, a.created_at,"
        + " c.full_name, c.email, c.phone, c.address"
        + " FROM accounts a JOIN customers c ON a.customer_id = c.customer_id"
        + " WHERE a.account_number = ?";
    try (Connection conn = getConn();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, accountNumber);
      ResultSet rs = ps.executeQuery();
      if (!rs.next())
        throw BankException.accountNotFound(accountNumber);
      return new String[] {
          rs.getString("account_number"),
          rs.getString("account_type"),
          String.format("%.2f", rs.getDouble("balance")),
          rs.getBoolean("is_active") ? "Active" : "Inactive",
          rs.getString("created_at"),
          rs.getString("full_name"),
          rs.getString("email"),
          rs.getString("phone"),
          rs.getString("address")
      };
    } catch (SQLException e) {
      throw new BankException("DB_ERROR", e.getMessage());
    }
  }

  public void deleteAccount(String accountNumber, String pin) throws BankException {
    Account acc = getAccountOrThrow(accountNumber);
    if (!HashUtil.verifyPin(pin, acc.getPin()))
      throw BankException.invalidPin();
    String sql = "UPDATE accounts SET is_active = FALSE WHERE account_number = ?";
    try (Connection conn = getConn();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, accountNumber);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new BankException("DB_ERROR", e.getMessage());
    }
  }

  public List<Transaction> getTransactionHistory(String accountNumber) throws BankException {
    getAccountOrThrow(accountNumber);
    List<Transaction> list = new ArrayList<>();
    String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC LIMIT 20";
    try (Connection conn = getConn();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, accountNumber);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        Transaction t = new Transaction();
        t.setTransactionId(rs.getInt("transaction_id"));
        t.setAccountNumber(rs.getString("account_number"));
        t.setTransactionType(Transaction.TransactionType.valueOf(rs.getString("transaction_type")));
        t.setAmount(rs.getDouble("amount"));
        t.setBalanceAfter(rs.getDouble("balance_after"));
        t.setDescription(rs.getString("description"));
        t.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
        list.add(t);
      }
    } catch (SQLException e) {
      throw new BankException("DB_ERROR", e.getMessage());
    }
    return list;
  }

  public Account getAccountOrThrow(String accountNumber) throws BankException {
    String sql = "SELECT * FROM accounts WHERE account_number = ?";
    try (Connection conn = getConn();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, accountNumber);
      ResultSet rs = ps.executeQuery();
      if (!rs.next())
        throw BankException.accountNotFound(accountNumber);
      if (!rs.getBoolean("is_active"))
        throw BankException.accountInactive();
      String type = rs.getString("account_type");
      int custId = rs.getInt("customer_id");
      double bal = rs.getDouble("balance");
      String pin = rs.getString("pin");
      if (type.equals("SAVINGS"))
        return new SavingsAccount(accountNumber, custId, bal, pin);
      else
        return new CurrentAccount(accountNumber, custId, bal, pin);
    } catch (SQLException e) {
      throw new BankException("DB_ERROR", e.getMessage());
    }
  }

  private void updateBalance(Connection conn, String accountNumber, double newBalance) throws SQLException {
    String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setDouble(1, newBalance);
      ps.setString(2, accountNumber);
      ps.executeUpdate();
    }
  }

  private void logTransaction(Connection conn, String accNo, String type,
      double amount, double balanceAfter, String desc) throws SQLException {
    String sql = "INSERT INTO transactions (account_number, transaction_type, amount, balance_after, description) VALUES (?,?,?,?,?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, accNo);
      ps.setString(2, type);
      ps.setDouble(3, amount);
      ps.setDouble(4, balanceAfter);
      ps.setString(5, desc);
      ps.executeUpdate();
    }
  }

  private void validateAmount(double amount) throws BankException {
    if (amount <= 0)
      throw BankException.invalidAmount();
  }
}
