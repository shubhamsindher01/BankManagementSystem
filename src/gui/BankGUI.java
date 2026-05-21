
package gui;

import model.*;
import service.AccountService;
import utils.BankException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BankGUI extends JFrame {

  private static final Color CLR_PRIMARY = new Color(26, 115, 232);
  private static final Color CLR_ACCENT = new Color(52, 168, 83);
  private static final Color CLR_DANGER = new Color(234, 67, 53);
  private static final Color CLR_BG = new Color(244, 246, 251);
  private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
  private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
  private static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 13);
  private static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 14);

  private final AccountService service = new AccountService();
  private JPanel centerPanel;

  public BankGUI() {
    setTitle("Bank Management System");
    setSize(1000, 700);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // Header
    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(CLR_PRIMARY);
    header.setBorder(BorderFactory.createEmptyBorder(16, 28, 16, 28));
    JLabel title = new JLabel("  Bank Management System");
    title.setFont(FONT_TITLE);
    title.setForeground(Color.WHITE);
    header.add(title, BorderLayout.WEST);
    add(header, BorderLayout.NORTH);

    // Center
    centerPanel = new JPanel(new BorderLayout());
    centerPanel.setBackground(CLR_BG);
    add(centerPanel, BorderLayout.CENTER);

    showMainMenu();
    setVisible(true);
  }

  private void showMainMenu() {
    centerPanel.removeAll();

    JPanel outer = new JPanel(new GridBagLayout());
    outer.setBackground(CLR_BG);

    JPanel inner = new JPanel(new BorderLayout(0, 20));
    inner.setBackground(CLR_BG);
    inner.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    JLabel sub = new JLabel("Select an option to get started", SwingConstants.CENTER);
    sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    sub.setForeground(Color.GRAY);
    inner.add(sub, BorderLayout.NORTH);

    JPanel grid = new JPanel(new GridLayout(3, 2, 16, 16));
    grid.setBackground(CLR_BG);
    grid.setPreferredSize(new Dimension(700, 360));

    grid.add(bigBtn("Create Account", "Create a new bank account", CLR_PRIMARY, e -> showCreateAccount()));
    grid.add(bigBtn("Deposit Money", "Add money to account", CLR_ACCENT, e -> showDeposit()));
    grid.add(bigBtn("Withdraw Money", "Take money from account", CLR_DANGER, e -> showWithdraw()));
    grid.add(bigBtn("Check Balance", "View current balance", CLR_PRIMARY, e -> showCheckBalance()));
    grid.add(bigBtn("Account Details", "View full account info", CLR_ACCENT, e -> showAccountDetails()));
    grid.add(bigBtn("Transaction History", "View past transactions", new Color(100, 100, 200),
        e -> showTransactionHistory()));

    inner.add(grid, BorderLayout.CENTER);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1;
    gbc.weighty = 1;
    outer.add(inner, gbc);

    centerPanel.add(outer, BorderLayout.CENTER);
    centerPanel.revalidate();
    centerPanel.repaint();
  }

  private JButton bigBtn(String title, String subtitle, Color bg, ActionListener al) {
    JPanel content = new JPanel(new GridLayout(2, 1));
    content.setOpaque(false);
    JLabel t = new JLabel(title, SwingConstants.CENTER);
    t.setFont(new Font("Segoe UI", Font.BOLD, 15));
    t.setForeground(Color.WHITE);
    JLabel s = new JLabel(subtitle, SwingConstants.CENTER);
    s.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    s.setForeground(new Color(220, 230, 255));
    content.add(t);
    content.add(s);

    JButton btn = new JButton();
    btn.setLayout(new BorderLayout());
    btn.add(content, BorderLayout.CENTER);
    btn.setBackground(bg);
    btn.setOpaque(true);
    btn.setBorderPainted(false);
    btn.setFocusPainted(false);
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btn.addActionListener(al);

    btn.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        btn.setBackground(bg.darker());
      }

      public void mouseExited(MouseEvent e) {
        btn.setBackground(bg);
      }
    });
    return btn;
  }

  private void showCreateAccount() {
    centerPanel.removeAll();
    JPanel form = formPanel("Create New Account");

    JTextField tfName = field();
    JTextField tfEmail = field();
    JTextField tfPhone = field();
    JTextField tfAddr = field();
    JComboBox<String> cbType = new JComboBox<>(new String[] { "SAVINGS", "CURRENT" });
    cbType.setFont(FONT_INPUT);
    cbType.setPreferredSize(new Dimension(300, 32));
    JTextField tfDep = field();
    JPasswordField tfPin = new JPasswordField();
    tfPin.setFont(FONT_INPUT);

    addRow(form, "Full Name", tfName);
    addRow(form, "Email", tfEmail);
    addRow(form, "Phone", tfPhone);
    addRow(form, "Address", tfAddr);
    addRow(form, "Account Type", cbType);
    addRow(form, "Initial Deposit", tfDep);
    addRow(form, "4-digit PIN", tfPin);

    JButton btnCreate = btn("Create Account", CLR_PRIMARY);
    JButton btnBack = btn("Back", Color.GRAY);
    btnBack.addActionListener(e -> showMainMenu());
    btnCreate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          Customer c = new Customer(tfName.getText(), tfEmail.getText(),
              tfPhone.getText(), tfAddr.getText());
          Account.AccountType t = Account.AccountType.valueOf((String) cbType.getSelectedItem());
          double dep = Double.parseDouble(tfDep.getText().trim());
          String pin = new String(tfPin.getPassword());
          Account acc = service.createAccount(c, t, dep, pin);
          JOptionPane.showMessageDialog(null,
              "Account Created Successfully!\n\nAccount Number: " + acc.getAccountNumber()
                  + "\nType: " + acc.getAccountType()
                  + "\nBalance: Rs." + String.format("%.2f", acc.getBalance())
                  + "\n\nPlease save your account number and PIN!",
              "Success", JOptionPane.INFORMATION_MESSAGE);
          showMainMenu();
        } catch (BankException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(null, "Invalid deposit amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    addButtons(form, btnBack, btnCreate);
    centerPanel.add(new JScrollPane(form), BorderLayout.CENTER);
    centerPanel.revalidate();
    centerPanel.repaint();
  }

  private void showDeposit() {
    centerPanel.removeAll();
    JPanel form = formPanel("Deposit Money");
    JTextField tfAcc = field(), tfAmt = field();
    addRow(form, "Account Number", tfAcc);
    addRow(form, "Amount (Rs.)", tfAmt);
    JButton btnDep = btn("Deposit", CLR_ACCENT), btnBack = btn("Back", Color.GRAY);
    btnBack.addActionListener(e -> showMainMenu());
    btnDep.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          double bal = service.deposit(tfAcc.getText().trim(), Double.parseDouble(tfAmt.getText().trim()));
          JOptionPane.showMessageDialog(null,
              "Deposit Successful!\nNew Balance: Rs." + String.format("%.2f", bal),
              "Success", JOptionPane.INFORMATION_MESSAGE);
          showMainMenu();
        } catch (BankException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(null, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    addButtons(form, btnBack, btnDep);
    centerPanel.add(new JScrollPane(form), BorderLayout.CENTER);
    centerPanel.revalidate();
    centerPanel.repaint();
  }

  private void showWithdraw() {
    centerPanel.removeAll();
    JPanel form = formPanel("Withdraw Money");
    JTextField tfAcc = field(), tfAmt = field();
    JPasswordField tfPin = new JPasswordField();
    tfPin.setFont(FONT_INPUT);
    addRow(form, "Account Number", tfAcc);
    addRow(form, "PIN", tfPin);
    addRow(form, "Amount (Rs.)", tfAmt);
    JButton btnWith = btn("Withdraw", CLR_DANGER), btnBack = btn("Back", Color.GRAY);
    btnBack.addActionListener(e -> showMainMenu());
    btnWith.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          double bal = service.withdraw(tfAcc.getText().trim(),
              new String(tfPin.getPassword()), Double.parseDouble(tfAmt.getText().trim()));
          JOptionPane.showMessageDialog(null,
              "Withdrawal Successful!\nRemaining Balance: Rs." + String.format("%.2f", bal),
              "Success", JOptionPane.INFORMATION_MESSAGE);
          showMainMenu();
        } catch (BankException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(null, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    addButtons(form, btnBack, btnWith);
    centerPanel.add(new JScrollPane(form), BorderLayout.CENTER);
    centerPanel.revalidate();
    centerPanel.repaint();
  }

  private void showCheckBalance() {
    centerPanel.removeAll();
    JPanel form = formPanel("Check Balance");
    JTextField tfAcc = field();
    JPasswordField tfPin = new JPasswordField();
    tfPin.setFont(FONT_INPUT);
    addRow(form, "Account Number", tfAcc);
    addRow(form, "PIN", tfPin);
    JButton btnChk = btn("Check Balance", CLR_PRIMARY), btnBack = btn("Back", Color.GRAY);
    btnBack.addActionListener(e -> showMainMenu());
    btnChk.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          double bal = service.checkBalance(tfAcc.getText().trim(), new String(tfPin.getPassword()));
          JOptionPane.showMessageDialog(null,
              "Account: " + tfAcc.getText().trim()
                  + "\nAvailable Balance: Rs." + String.format("%.2f", bal),
              "Balance", JOptionPane.INFORMATION_MESSAGE);
        } catch (BankException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    addButtons(form, btnBack, btnChk);
    centerPanel.add(new JScrollPane(form), BorderLayout.CENTER);
    centerPanel.revalidate();
    centerPanel.repaint();
  }

  private void showAccountDetails() {
    centerPanel.removeAll();
    JPanel form = formPanel("Account Details");
    JTextField tfAcc = field();
    addRow(form, "Account Number", tfAcc);
    JButton btnView = btn("View Details", CLR_ACCENT), btnBack = btn("Back", Color.GRAY);
    btnBack.addActionListener(e -> showMainMenu());
    btnView.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          String[] d = service.getAccountDetails(tfAcc.getText().trim());
          JOptionPane.showMessageDialog(null,
              "Account Number : " + d[0] + "\n"
                  + "Account Type   : " + d[1] + "\n"
                  + "Balance        : Rs." + d[2] + "\n"
                  + "Status         : " + d[3] + "\n"
                  + "Opened On      : " + d[4] + "\n"
                  + "─────────────────────────\n"
                  + "Customer Name  : " + d[5] + "\n"
                  + "Email          : " + d[6] + "\n"
                  + "Phone          : " + d[7] + "\n"
                  + "Address        : " + d[8],
              "Account Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (BankException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    addButtons(form, btnBack, btnView);
    centerPanel.add(new JScrollPane(form), BorderLayout.CENTER);
    centerPanel.revalidate();
    centerPanel.repaint();
  }

  private void showTransactionHistory() {
    centerPanel.removeAll();
    JPanel form = formPanel("Transaction History");
    JTextField tfAcc = field();
    addRow(form, "Account Number", tfAcc);
    JButton btnFetch = btn("Fetch History", CLR_PRIMARY), btnBack = btn("Back", Color.GRAY);
    btnBack.addActionListener(e -> showMainMenu());
    btnFetch.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          List<Transaction> txns = service.getTransactionHistory(tfAcc.getText().trim());
          if (txns.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No transactions found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
          }
          String[] cols = { "Date & Time", "Type", "Amount", "Balance", "Description" };
          Object[][] rows = new Object[txns.size()][5];
          for (int i = 0; i < txns.size(); i++) {
            Transaction t = txns.get(i);
            rows[i][0] = t.getFormattedDate();
            rows[i][1] = t.getTransactionType();
            rows[i][2] = "Rs." + String.format("%.2f", t.getAmount());
            rows[i][3] = "Rs." + String.format("%.2f", t.getBalanceAfter());
            rows[i][4] = t.getDescription();
          }
          JTable table = new JTable(rows, cols);
          table.setFont(FONT_INPUT);
          table.setRowHeight(28);
          table.getTableHeader().setFont(FONT_LABEL);
          table.setEnabled(false);
          JScrollPane sp = new JScrollPane(table);
          sp.setPreferredSize(new Dimension(800, 320));
          JOptionPane.showMessageDialog(null, sp, "Transactions", JOptionPane.PLAIN_MESSAGE);
        } catch (BankException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    addButtons(form, btnBack, btnFetch);
    centerPanel.add(new JScrollPane(form), BorderLayout.CENTER);
    centerPanel.revalidate();
    centerPanel.repaint();
  }

  // ── Helpers ──────────────────────────────
  private JPanel formPanel(String title) {
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    p.setBackground(Color.WHITE);
    p.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
    JLabel lbl = new JLabel(title);
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
    lbl.setForeground(CLR_PRIMARY);
    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
    p.add(lbl);
    p.add(Box.createVerticalStrut(20));
    return p;
  }

  private void addRow(JPanel form, String labelText, JComponent field) {
    JPanel row = new JPanel(new BorderLayout(12, 0));
    row.setBackground(Color.WHITE);
    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    row.setAlignmentX(Component.LEFT_ALIGNMENT);
    JLabel lbl = new JLabel(labelText + ":");
    lbl.setFont(FONT_LABEL);
    lbl.setPreferredSize(new Dimension(160, 32));
    if (field instanceof JTextField)
      ((JTextField) field).setPreferredSize(new Dimension(300, 32));
    row.add(lbl, BorderLayout.WEST);
    row.add(field, BorderLayout.CENTER);
    form.add(row);
    form.add(Box.createVerticalStrut(12));
  }

  private void addButtons(JPanel form, JButton back, JButton action) {
    form.add(Box.createVerticalStrut(10));
    JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
    btnRow.setBackground(Color.WHITE);
    btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
    btnRow.add(back);
    btnRow.add(action);
    form.add(btnRow);
  }

  private JTextField field() {
    JTextField t = new JTextField();
    t.setFont(FONT_INPUT);
    t.setPreferredSize(new Dimension(300, 32));
    return t;
  }

  private JButton btn(String text, Color bg) {
    JButton b = new JButton(text);
    b.setFont(FONT_BTN);
    b.setBackground(bg);
    b.setForeground(Color.WHITE);
    b.setFocusPainted(false);
    b.setBorderPainted(false);
    b.setOpaque(true);
    b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    b.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
    return b;
  }
}
