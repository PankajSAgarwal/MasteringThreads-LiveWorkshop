package masteringthreads.ch3_the_secrets_of_concurrency.exercise_3_2;

public class BankAccountTest {
    public static void main(String... args) {
        // create a BankAccount instance
        // create a Runnable lambda
        // in the run() method, call repeatedly:
        // account.deposit(100);
        // account.withdraw(100);
        // create two thread instances, both pointing at
        // your Runnable
        // create a timer task to once a second prints
        // out the balance
        //
        // Balance should be 1000, 1100 or 1200, nothing else
    }
}
