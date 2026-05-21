package model;

/**
 * Customer - Model class
 * Demonstrates: Encapsulation, OOP
 */
public class Customer {

  private int customerId;
  private String fullName;
  private String email;
  private String phone;
  private String address;

  // Constructors
  public Customer() {
  }

  public Customer(String fullName, String email, String phone, String address) {
    this.fullName = fullName;
    this.email = email;
    this.phone = phone;
    this.address = address;
  }

  public Customer(int customerId, String fullName, String email, String phone, String address) {
    this.customerId = customerId;
    this.fullName = fullName;
    this.email = email;
    this.phone = phone;
    this.address = address;
  }

  // Getters & Setters (Encapsulation)
  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return String.format("Customer[ID=%d, Name=%s, Email=%s, Phone=%s]",
        customerId, fullName, email, phone);
  }
}



// run in console mode - java -cp ".:out:lib/mysql-connector-j-8.3.0.jar" Main console
// run in gyi mode- java -cp ".:out:lib/mysql-connector-j-8.3.0.jar" Main
 