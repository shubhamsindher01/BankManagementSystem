package model;

/**
 * Account - Base model class
 * Demonstrates: Encapsulation, Inheritance (base for SavingsAccount /
 * CurrentAccount)
 */
public class Account {

  public enum AccountType {
    SAVINGS, CURRENT
  }

  private String accountNumber;
  private int customerId;
  private AccountType accountType;
  private double balance;
  private String pin; // stored as SHA-256 hash
  private boolean isActive;

  // Constructors
  public Account() {
  }

  public Account(String accountNumber, int customerId, AccountType accountType,
      double balance, String pin) {
    this.accountNumber = accountNumber;
    this.customerId = customerId;
    this.accountType = accountType;
    this.balance = balance;
    this.pin = pin;
    this.isActive = true;
  }

  // Getters & Setters
  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public AccountType getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  // Polymorphism – overridden in subclasses
  public double getInterestRate() {
    return 0.0;
  }

  @Override
  public String toString() {
    return String.format("Account[No=%s, Type=%s, Balance=%.2f, Active=%b]",
        accountNumber, accountType, balance, isActive);
  }
}