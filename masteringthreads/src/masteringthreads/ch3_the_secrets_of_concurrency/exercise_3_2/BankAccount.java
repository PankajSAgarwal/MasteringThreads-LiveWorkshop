package masteringthreads.ch3_the_secrets_of_concurrency.exercise_3_2;

// DO NOT CHANGE
public class BankAccount {
    private int balance;

    public BankAccount(int initial) {
        balance = initial;
    }

    public void deposit(int amount) {
        balance = balance + amount;
    }

    public void withdraw(int amount) {
        deposit(-amount);
    }

    public int getBalance() {
        return balance;
    }
}
