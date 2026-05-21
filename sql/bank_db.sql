-- BANK MANAGEMENT SYSTEM - SQLITE SCHEMA

-- Customers Table
CREATE TABLE IF NOT EXISTS customers (
    customer_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    full_name       TEXT NOT NULL,
    email           TEXT UNIQUE NOT NULL,
    phone           TEXT NOT NULL,
    address         TEXT,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Accounts Table
CREATE TABLE IF NOT EXISTS accounts (
    account_number  TEXT PRIMARY KEY,
    customer_id     INTEGER NOT NULL,
    account_type    TEXT CHECK(account_type IN ('SAVINGS', 'CURRENT')) DEFAULT 'SAVINGS',
    balance         REAL DEFAULT 0.00,
    pin             TEXT NOT NULL,
    is_active       INTEGER DEFAULT 1,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (customer_id)
    REFERENCES customers(customer_id)
    ON DELETE CASCADE
);

-- Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    account_number   TEXT NOT NULL,
    transaction_type TEXT CHECK(transaction_type IN ('DEPOSIT','WITHDRAWAL','INTEREST')) NOT NULL,
    amount           REAL NOT NULL,
    balance_after    REAL NOT NULL,
    description      TEXT,
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (account_number)
    REFERENCES accounts(account_number)
);

-- Sample Customer
INSERT INTO customers (full_name, email, phone, address)
VALUES (
    'Admin User',
    'admin@bank.com',
    '9999999999',
    'Bank HQ'
);

-- Sample Account
-- SHA-256 hash for PIN 1234
INSERT INTO accounts (
    account_number,
    customer_id,
    account_type,
    balance,
    pin
)
VALUES (
    'ACC0000001',
    1,
    'SAVINGS',
    50000.00,
    '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4'
);