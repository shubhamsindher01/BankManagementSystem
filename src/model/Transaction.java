package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transaction - Model class for transaction history
 * Demonstrates: Encapsulation, OOP
 */
public class Transaction {

  public enum TransactionType {
    DEPOSIT, WITHDRAWAL, INTEREST
  }

  private int transactionId;
  private String accountNumber;
  private TransactionType transactionType;
  private double amount;
  private double balanceAfter;
  private String description;
  private LocalDateTime transactionDate;

  // Constructors
  public Transaction() {
  }

  public Transaction(String accountNumber, TransactionType transactionType,
      double amount, double balanceAfter, String description) {
    this.accountNumber = accountNumber;
    this.transactionType = transactionType;
    this.amount = amount;
    this.balanceAfter = balanceAfter;
    this.description = description;
    this.transactionDate = LocalDateTime.now();
  }

  // Getters & Setters
  public int getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(int id) {
    this.transactionId = id;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionType t) {
    this.transactionType = t;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public double getBalanceAfter() {
    return balanceAfter;
  }

  public void setBalanceAfter(double b) {
    this.balanceAfter = b;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String d) {
    this.description = d;
  }

  public LocalDateTime getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(LocalDateTime d) {
    this.transactionDate = d;
  }

  public String getFormattedDate() {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    return transactionDate != null ? transactionDate.format(fmt) : "N/A";
  }

  @Override
  public String toString() {
    return String.format("[%s] %-12s | Amount: %10.2f | Balance: %10.2f | %s",
        getFormattedDate(), transactionType, amount, balanceAfter, description);
  }
}