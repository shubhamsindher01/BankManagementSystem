package utils;

/**
 * BankException - Custom Exception class
 * Demonstrates: Exception Handling (custom exceptions)
 */
public class BankException extends Exception {

  private final String errorCode;

  public BankException(String message) {
    super(message);
    this.errorCode = "BANK_ERR";
  }

  public BankException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }

  // Common error factory methods
  public static BankException insufficientFunds() {
    return new BankException("INSUFFICIENT_FUNDS", "Insufficient balance for this transaction.");
  }

  public static BankException accountNotFound(String accNo) {
    return new BankException("ACCOUNT_NOT_FOUND", "Account not found: " + accNo);
  }

  public static BankException invalidPin() {
    return new BankException("INVALID_PIN", "Incorrect PIN. Please try again.");
  }

  public static BankException accountInactive() {
    return new BankException("ACCOUNT_INACTIVE", "This account has been deactivated.");
  }

  public static BankException duplicateEmail(String email) {
    return new BankException("DUPLICATE_EMAIL", "Email already registered: " + email);
  }

  public static BankException invalidAmount() {
    return new BankException("INVALID_AMOUNT", "Amount must be greater than zero.");
  }
}