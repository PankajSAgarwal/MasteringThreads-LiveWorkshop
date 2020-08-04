package masteringthreads.ch3_the_secrets_of_concurrency.exercise_3_2;

import java.util.*;

public class BankAccountTest {
    public static void main(String... args) {
        var account = new BankAccount(1000);
        Runnable depositWithdraw = () -> {
            while (true) {
                account.deposit(100);
                account.withdraw(100);
            }
        };
        new Thread(depositWithdraw).start();
        doSomeMagic();
        new Thread(depositWithdraw).start();

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(account.getBalance());
            }
        }, 1000, 1000);
    }

    private static void doSomeMagic() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
