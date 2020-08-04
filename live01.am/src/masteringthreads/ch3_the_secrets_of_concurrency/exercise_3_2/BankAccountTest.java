package masteringthreads.ch3_the_secrets_of_concurrency.exercise_3_2;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class BankAccountTest {
    private static volatile BankAccount account = new BankAccount(1000);

    public static void main(String... args) throws InterruptedException, ExecutionException {
        Runnable depositWithdraw = () -> {
            while(true) {
                account.deposit(100);
                account.withdraw(100);
            }
        };
        new Thread(depositWithdraw).start();
//        doSomeMagic();
        new Thread(depositWithdraw).start();

        Timer timer = new Timer(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timer: " + account.getBalance());
            }
        }, 1000, 1000);

        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor(
            new ThreadFactory() {
                private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = defaultFactory.newThread(r);
                    thread.setDaemon(false);
                    return thread;
                }
            }
        );
        ScheduledFuture<?> scheduledFuture = ses.scheduleWithFixedDelay(() -> {
            System.out.println("ScheduledExecutorService: " + account.getBalance());
        }, 1, 1, TimeUnit.SECONDS);

        Thread.sleep(5500);
        account = null;
        Thread.sleep(500);
        System.out.println(scheduledFuture.get());

    }

    private static void doSomeMagic() {
        LockSupport.parkNanos(100_000_000);
    }
}
