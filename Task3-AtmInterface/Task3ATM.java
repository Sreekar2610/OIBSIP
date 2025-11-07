import java.util.*;

public class Task3ATM {
    public static void main(String[] args) {
        UserAccount user = new UserAccount("sreekar2610", "1234", 25000.0);

        Scanner scanner = new Scanner(System.in);

        System.out.println("====== Welcome to Java ATM ======");

        while (true) {
            System.out.print("Enter Customer User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter Customer PIN: ");
            String pin = scanner.nextLine();

            if (user.login(userId, pin)) {
                System.out.println("Login Successful!");
                break;
            } else {
                System.out.println("Invalid ID or PIN. Try again.\n");
            }
        }

        while (true) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Select option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    user.showTransactionHistory();
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine();
                    user.withdraw(withdrawAmount);
                    break;
                case 3:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine();
                    user.deposit(depositAmount);
                    break;
                case 4:
                    System.out.print("Enter recipient user ID: ");
                    String recipientId = scanner.nextLine();
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine();

                    if (recipientId.equals(user.getUserId())) {
                        System.out.println("Cannot transfer to your own account!");
                    } else {
                        System.out.println("Recipient not found (for demo). No action taken.");
                    }
                    break;
                case 5:
                    System.out.println("Thank you for using Java ATM!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}

class UserAccount {
    private String userId, userPin;
    private double balance;
    private List<String> transactions;

    public UserAccount(String userId, String userPin, double balance) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public boolean login(String inputId, String inputPin) {
        return (userId.equals(inputId) && userPin.equals(inputPin));
    }

    public void showTransactionHistory() {
        System.out.println("- Transaction History -");
        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (String t : transactions) {
                System.out.println(t);
            }
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            transactions.add("Withdraw: -" + amount + " | Balance: " + balance);
            System.out.println("Withdrawn successfully. Current Balance: " + balance);
        } else {
            System.out.println("Insufficient balance or invalid amount??.");
        }
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add("Deposit: +" + amount + " | Balance: " + balance);
            System.out.println("Deposited successfully . Current Balance: " + balance);
        } else {
            System.out.println("Invalid deposit amount!!.");
        }
    }

    public String getUserId() {
        return userId;
    }
}
