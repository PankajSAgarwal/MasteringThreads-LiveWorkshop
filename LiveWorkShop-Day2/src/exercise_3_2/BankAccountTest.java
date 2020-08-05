package exercise_3_2;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.LockSupport;

public class BankAccountTest {
    public static void main(String... args) {
        // create a BankAccount instance
        var account = new BankAccount(1000);
        // create a Runnable lambda
        // in the run() method, call repeatedly:
        // account.deposit(100);
        // account.withdraw(100);
        Runnable depositWithdraw = ()->{
            while (true){
                account.deposit(100);
                account.withdraw(100);
            }
        };
        // create two thread instances, both pointing at
        // your Runnable
        new Thread(depositWithdraw).start();
        // this method will always print balance as 1000
        // this doesn't work if we use -Xint or XX:-Inline VM options
        doSomeMagic();
        new Thread(depositWithdraw).start();
        // create a timer task to once a second prints
        // out the balance
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(account.getBalance());
            }
        },1000,1000);
        //
        // Balance should be 1000, 1100 or 1200, nothing else
    }

    private static void doSomeMagic() {
        LockSupport.parkNanos(100_000_000);// sleep for 100 ms
    }
}
