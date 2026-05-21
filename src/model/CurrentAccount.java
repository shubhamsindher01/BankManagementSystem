package model;

/**
 * CurrentAccount - Inherits Account
 * Demonstrates: Inheritance, Polymorphism
 */
public class CurrentAccount extends Account {

  private static final double INTEREST_RATE = 2.0; // 2% per annum
  private static final double OVERDRAFT_LIMIT = 5000.0;

  public CurrentAccount(String accountNumber, int customerId,
      double balance, String pin) {
    super(accountNumber, customerId, AccountType.CURRENT, balance, pin);
  }

  @Override
  public double getInterestRate() {
    return INTEREST_RATE;
  }

  public double getOverdraftLimit() {
    return OVERDRAFT_LIMIT;
  }

  public boolean canWithdraw(double amount) {
    return (getBalance() + OVERDRAFT_LIMIT) >= amount;
  }
}