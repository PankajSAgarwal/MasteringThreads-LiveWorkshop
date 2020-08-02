package playground;

public class RaceConditionBankAccountExample {
    private double balance;

    public RaceConditionBankAccountExample(double initial) {
        this.balance = initial;
    }

    public void deposit(double amount){
        balance = balance + amount;
    }

    public void withdraw(double amount){
        deposit(-amount);
    }

}
