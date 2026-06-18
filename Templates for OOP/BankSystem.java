import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * EXAM QUESTION (typical phrasing):
 * Design a bank management system with the following requirements:
 * a) Each account has a unique account number, owner name, and balance.
 * b) There are two types of accounts: SavingsAccount (earns interest, has a
 *    minimum balance requirement) and CurrentAccount (allows overdraft up to
 *    a limit). Use inheritance and polymorphism.
 * c) Implement deposit and withdraw methods with proper validation
 *    (e.g. cannot withdraw below minimum balance / beyond overdraft limit).
 * d) Implement a method to transfer money between two accounts.
 * e) Money transfers must be safe even if multiple transfers happen at the
 *    same time (concurrency).
 * f) Implement a method to display a mini-statement (transaction history).
 * g) Ensure encapsulation and validate all inputs; use exceptions for invalid
 *    operations.
 *
 * DESIGN NOTES:
 * - Account (abstract) -> SavingsAccount, CurrentAccount: inheritance,
 *   polymorphism via withdraw() rules and applyMonthlyInterest().
 * - Each account keeps its own transaction log (encapsulated).
 * - transfer() is synchronized on both accounts using a fixed lock ordering
 *   (by account number) to avoid deadlock when two transfers happen in
 *   opposite directions at the same time.
 */

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

abstract class Account {
    protected final String accountNumber;
    protected final String ownerName;
    protected double balance;
    protected final List<String> transactionHistory;

    public Account(String accountNumber, String ownerName, double initialBalance) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be empty.");
        }
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be empty.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        log("Account opened with balance " + initialBalance);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public synchronized double getBalance() {
        return balance;
    }

    protected void log(String entry) {
        transactionHistory.add(entry);
    }

    public synchronized void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
        log("Deposited " + amount + " | New balance: " + balance);
    }

    // Each subtype defines its own withdrawal rule -> polymorphism
    public abstract void withdraw(double amount) throws InsufficientFundsException;

    public synchronized void displayStatement() {
        System.out.println("Statement for account " + accountNumber + " (" + ownerName + "):");
        for (String entry : transactionHistory) {
            System.out.println("  - " + entry);
        }
        System.out.println("  Current balance: " + balance);
    }
}

class SavingsAccount extends Account {
    private static final double MIN_BALANCE = 1000.0;
    private double interestRate; // e.g. 0.05 = 5%

    public SavingsAccount(String accountNumber, String ownerName, double initialBalance, double interestRate) {
        super(accountNumber, ownerName, initialBalance);
        if (initialBalance < MIN_BALANCE) {
            throw new IllegalArgumentException("Savings account must open with at least " + MIN_BALANCE);
        }
        if (interestRate < 0 || interestRate > 1) {
            throw new IllegalArgumentException("Interest rate must be between 0 and 1.");
        }
        this.interestRate = interestRate;
    }

    @Override
    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (balance - amount < MIN_BALANCE) {
            throw new InsufficientFundsException(
                    "Cannot withdraw " + amount + ": savings accounts must keep a minimum balance of " + MIN_BALANCE);
        }
        balance -= amount;
        log("Withdrew " + amount + " | New balance: " + balance);
    }

    public synchronized void applyMonthlyInterest() {
        double interest = balance * interestRate;
        balance += interest;
        log("Interest applied: " + interest + " | New balance: " + balance);
    }
}

class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String ownerName, double initialBalance, double overdraftLimit) {
        super(accountNumber, ownerName, initialBalance);
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (balance - amount < -overdraftLimit) {
            throw new InsufficientFundsException(
                    "Cannot withdraw " + amount + ": exceeds overdraft limit of " + overdraftLimit);
        }
        balance -= amount;
        log("Withdrew " + amount + " | New balance: " + balance);
    }
}

// ---------- Bank: handles safe transfers ----------
class Bank {
    public void transfer(Account from, Account to, double amount) throws InsufficientFundsException {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Accounts cannot be null.");
        }
        if (from == to) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }

        // Lock accounts in a CONSISTENT order (by account number) to prevent deadlock
        // when two transfers happen in opposite directions concurrently.
        Account first = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0 ? from : to;
        Account second = first == from ? to : from;

        synchronized (first) {
            synchronized (second) {
                from.withdraw(amount);   // throws if insufficient funds; nothing is deposited yet
                to.deposit(amount);
                System.out.println("Transferred " + amount + " from " + from.getAccountNumber() +
                        " to " + to.getAccountNumber());
            }
        }
    }
}

// ---------- Runnable for concurrent transfer simulation ----------
class TransferTask implements Runnable {
    private final Bank bank;
    private final Account from;
    private final Account to;
    private final double amount;

    public TransferTask(Bank bank, Account from, Account to, double amount) {
        this.bank = bank;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            bank.transfer(from, to, amount);
        } catch (InsufficientFundsException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
}

// ---------- Demo ----------
public class BankSystem {
    public static void main(String[] args) {
        try {
            Bank bank = new Bank();
            SavingsAccount acc1 = new SavingsAccount("SAV001", "Diane", 5000, 0.05);
            CurrentAccount acc2 = new CurrentAccount("CUR001", "Eric", 2000, 1000);

            // Simulate concurrent transfers happening in opposite directions
            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.execute(new TransferTask(bank, acc1, acc2, 1000));
            executor.execute(new TransferTask(bank, acc2, acc1, 500));

            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);

            acc1.displayStatement();
            acc2.displayStatement();

            acc1.applyMonthlyInterest();
            acc1.displayStatement();

            // This should fail - breaks minimum balance rule
            acc1.withdraw(100000);

        } catch (InsufficientFundsException e) {
            System.out.println("Transaction error: " + e.getMessage());
        } catch (IllegalArgumentException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
