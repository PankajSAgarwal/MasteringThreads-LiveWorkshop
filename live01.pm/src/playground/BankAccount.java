package playground;

public class BankAccount {
    private double balance;
    private final Object monitor = new Object();

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        synchronized (monitor) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        deposit(-amount);
    }

    public double getBalance() {
        synchronized (monitor) {
            return balance;
        }
    }
}
