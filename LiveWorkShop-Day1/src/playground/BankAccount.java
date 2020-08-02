package playground;

public class BankAccount {
    private final Object monitor = new Object();
    private double balance;

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount){
        synchronized (monitor){
            balance = balance + amount;
        }
    }

    public void withdraw(double amount){
        deposit(-amount);
    }
}
