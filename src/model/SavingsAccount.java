package model;

public class SavingsAccount extends Account {

    private static final double INTEREST_RATE = 4.5;

    public SavingsAccount(String accountNumber, int customerId,
                          double balance, String pin) {
        super(accountNumber, customerId, AccountType.SAVINGS, balance, pin);
    }

    @Override
    public double getInterestRate() { return INTEREST_RATE; }

    public double calculateMonthlyInterest() {
        return (getBalance() * INTEREST_RATE) / (100 * 12);
    }
}
