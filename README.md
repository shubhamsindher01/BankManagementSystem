# 🏦 Bank Management System — Java Project

A full-featured banking application demonstrating core Java concepts.

\---

## 📁 Project Structure

```
BankManagementSystem/
│
├── sql/
│   └── bank\_db.sql              ← MySQL schema \& sample data
│
├── src/
│   ├── Main.java                ← Entry point (GUI or Console)
│   │
│   ├── model/                   ← OOP Model classes
│   │   ├── Account.java         ← Base class (Encapsulation)
│   │   ├── SavingsAccount.java  ← Inherits Account
│   │   ├── CurrentAccount.java  ← Inherits Account
│   │   ├── Customer.java        ← Customer model
│   │   └── Transaction.java     ← Transaction model
│   │
│   ├── database/
│   │   └── DatabaseConnection.java  ← Singleton JDBC connection
│   │
│   ├── service/
│   │   └── AccountService.java  ← All business logic
│   │
│   ├── gui/
│   │   ├── BankGUI.java         ← Swing GUI (Option 3)
│   │   └── ConsoleApp.java      ← Console interface (Option 1)
│   │
│   └── utils/
│       ├── BankException.java        ← Custom exception
│       ├── HashUtil.java             ← SHA-256 PIN hashing
│       └── AccountNumberGenerator.java
│
└── README.md
```

\---

## ✅ OOP Concepts Used

|Concept|Where|
|-|-|
|**Encapsulation**|All model classes (private fields + getters/setters)|
|**Inheritance**|`SavingsAccount` \& `CurrentAccount` extend `Account`|
|**Polymorphism**|`getInterestRate()` overridden in subclasses|
|**Abstraction**|Service layer hides DB logic from GUI|
|**Exception Handling**|`BankException` with custom error codes|
|**Design Patterns**|Singleton in `DatabaseConnection`|

\---

## ⚙️ Setup Instructions (SQLite + TablePlus)

### 1\. Download SQLite JDBC Driver

Get `sqlite-jdbc-x.x.x.jar` from:
https://github.com/xerial/sqlite-jdbc/releases

Place it in your project folder (e.g. `lib/sqlite-jdbc.jar`).

### 2\. No database setup needed!

The Java app **automatically creates** `bank\_management.db`
and all tables on first run. Nothing to configure. ✅

### 3\. Compile \& Run

**GUI Mode (Swing):**

```bash
javac -cp "lib/sqlite-jdbc.jar.jar" -d out src/database/\*.java src/gui/\*.java src/model/\*.java src/service/\*.java src/utils/\*.java src/Main.java

java -cp "out;lib/sqlite-jdbc.jar.jar" Main
```

**Console Mode:**

```bash
java -cp "out;lib/sqlite-jdbc.jar.jar" Main console
```

> On Linux/Mac use `:` instead of `;` in classpath.

### 4\. Open in TablePlus

1. Open **TablePlus**
2. Click **+ New Connection → SQLite**
3. Browse to `bank\_management.db` (in your project run folder)
4. Click **Connect** — you'll see all your tables and data live! 🎉

### 5\. Load sample data (optional)

Run `sql/bank\_db.sql` in TablePlus to add a test account.
Sample account: `ACC0000001` | PIN: `1234`

\---

## 🌟 Features

|Feature|Status|
|-|-|
|Create Account|✅|
|Deposit Money|✅|
|Withdraw Money|✅|
|Check Balance|✅|
|View Account Details|✅|
|Transaction History|✅|
|Delete Account|✅|
|PIN Security (SHA-256)|✅|
|Interest Calculation|✅|
|Savings \& Current Account Types|✅|
|Swing GUI|✅|
|MySQL Database|✅|
|JDBC Integration|✅|
|Custom Exceptions|✅|

\---

## 🏫 For Teachers

This project demonstrates:

* **4 OOP Pillars** — Encapsulation, Inheritance, Polymorphism, Abstraction
* **Exception Handling** — Custom `BankException` with error codes
* **Database** — MySQL with JDBC and PreparedStatements (SQL injection safe)
* **Security** — SHA-256 PIN hashing (no plain-text passwords)
* **Design Pattern** — Singleton for DB connection
* **GUI** — Professional Java Swing interface
* **Layered Architecture** — Model → Service → GUI (MVC-inspired)

