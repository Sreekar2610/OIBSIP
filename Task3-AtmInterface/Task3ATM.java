import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Task3ATM extends JFrame {
    private UserAccount user = new UserAccount("sreekar2610", "1234", 25000.0);

    private JPanel loginPanel, menuPanel, transactionPanel;
    private JTextField userIdField, pinField;
    private JLabel statusLabel, balanceLabel;
    private JTextArea transactionArea;

    public Task3ATM() {
        setTitle("Java ATM (Swing)");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showLoginPanel();
    }

    private void showLoginPanel() {
        loginPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        userIdField = new JTextField();
        pinField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        statusLabel = new JLabel(" ");

        loginPanel.add(new JLabel("Customer User ID:"));
        loginPanel.add(userIdField);
        loginPanel.add(new JLabel("Customer PIN:"));
        loginPanel.add(pinField);
        loginPanel.add(loginBtn);
        loginPanel.add(statusLabel);

        setContentPane(loginPanel);
        revalidate();
        repaint();

        loginBtn.addActionListener(e -> {
            String inputId = userIdField.getText();
            String inputPin = pinField.getText();
            if (user.login(inputId, inputPin)) {
                showMenuPanel();
            } else {
                statusLabel.setText("Invalid ID or PIN. Try again.");
            }
        });
    }

    private void showMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(8, 1, 10, 10)); // increased row count

        balanceLabel = new JLabel("Balance: ₹" + user.getBalance());
        JButton txnBtn = new JButton("Transaction History");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton depositBtn = new JButton("Deposit");
        JButton transferBtn = new JButton("Transfer");
        JButton quitBtn = new JButton("Quit");

        menuPanel.add(new JLabel("ATM Menu:"));
        menuPanel.add(balanceLabel);
        menuPanel.add(txnBtn);
        menuPanel.add(withdrawBtn);
        menuPanel.add(depositBtn);
        menuPanel.add(transferBtn); // added transfer button
        menuPanel.add(quitBtn);

        setContentPane(menuPanel);
        revalidate();
        repaint();

        txnBtn.addActionListener(e -> showTransactionPanel());
        withdrawBtn.addActionListener(e -> withdrawDialog());
        depositBtn.addActionListener(e -> depositDialog());
        transferBtn.addActionListener(e -> transferDialog());
        quitBtn.addActionListener(e -> System.exit(0));
    }

    private void showTransactionPanel() {
        transactionPanel = new JPanel(new BorderLayout());
        transactionArea = new JTextArea();
        transactionArea.setEditable(false);

        StringBuilder history = new StringBuilder();
        for (String t : user.getTransactions()) {
            history.append(t).append("\n");
        }
        if (history.length() == 0) history.append("No transactions yet.\n");
        transactionArea.setText(history.toString());

        JButton backBtn = new JButton("Back");
        transactionPanel.add(new JLabel("Transaction History"), BorderLayout.NORTH);
        transactionPanel.add(new JScrollPane(transactionArea), BorderLayout.CENTER);
        transactionPanel.add(backBtn, BorderLayout.SOUTH);

        setContentPane(transactionPanel);
        revalidate();
        repaint();

        backBtn.addActionListener(e -> showMenuPanel());
    }

    private void withdrawDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (input != null) {
            double amt;
            try {
                amt = Double.parseDouble(input);
                if (user.withdraw(amt)) {
                    JOptionPane.showMessageDialog(this, "Withdraw successful. Balance: ₹" + user.getBalance());
                    balanceLabel.setText("Balance: ₹" + user.getBalance());
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient balance or invalid amount.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number.");
            }
        }
    }

    private void depositDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (input != null) {
            double amt;
            try {
                amt = Double.parseDouble(input);
                if (user.deposit(amt)) {
                    JOptionPane.showMessageDialog(this, "Deposit successful. Balance: ₹" + user.getBalance());
                    balanceLabel.setText("Balance: ₹" + user.getBalance());
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid deposit amount.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number.");
            }
        }
    }

    private void transferDialog() {
        String recipient = JOptionPane.showInputDialog(this, "Enter recipient user ID:");
        if (recipient == null) return;
        if (recipient.equals(user.getUserId())) {
            JOptionPane.showMessageDialog(this, "Cannot transfer to your own account!");
            return;
        }
        String amtInput = JOptionPane.showInputDialog(this, "Enter amount to transfer:");
        if (amtInput == null) return;
        try {
            double amt = Double.parseDouble(amtInput);
            if (user.transfer(amt, recipient)) {
                JOptionPane.showMessageDialog(this, "Transferred ₹" + amt + " to " + recipient + ". Balance: ₹" + user.getBalance());
                balanceLabel.setText("Balance: ₹" + user.getBalance());
            } else {
                JOptionPane.showMessageDialog(this, "Transfer failed. Insufficient balance or invalid amount.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task3ATM().setVisible(true));
    }
}

class UserAccount {
    private String userId, userPin;
    private double balance;
    private java.util.List<String> transactions;

    public UserAccount(String userId, String userPin, double balance) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = balance;
        this.transactions = new java.util.ArrayList<>();
    }

    public boolean login(String inputId, String inputPin) {
        return userId.equals(inputId) && userPin.equals(inputPin);
    }

    public java.util.List<String> getTransactions() {
        return transactions;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            transactions.add("Withdraw: -" + amount + " | Balance: " + balance);
            return true;
        }
        return false;
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add("Deposit: +" + amount + " | Balance: " + balance);
            return true;
        }
        return false;
    }

    public boolean transfer(double amount, String recipientId) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            transactions.add("Transferred: -" + amount + " to " + recipientId + " | Balance: " + balance);
            return true;
        }
        return false;
    }

    public String getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }
}
