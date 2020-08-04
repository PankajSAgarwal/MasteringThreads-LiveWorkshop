package playground;

public class Bank {
    public void withdrawIfSufficientFunds(BankAccount account, double amount) {
        synchronized (account) {
            if (amount <= account.getBalance()) {
                account.withdraw(amount);
            }
        }
    }
}
