package playground;

import java.util.concurrent.*;

public class Interruptions {
    public static void main(String... args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) ;
            System.out.println("t1 Interrupted: " + Thread.currentThread().isInterrupted());
        });
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                System.out.println("t2 Interrupted: " + Thread.currentThread().isInterrupted());
            }
        });
        Thread t3 = new Thread(() -> {
            while (true) {
                if (Thread.interrupted()) {
                    System.out.println("t3 Interrupted: " + Thread.currentThread().isInterrupted());
                    break;
                }
            }
        });
        Semaphore semaphore = new Semaphore(0);
        Thread t4 = new Thread(() -> {
            semaphore.acquireUninterruptibly();
            System.out.println("t4 Interrupted: " + Thread.currentThread().isInterrupted());
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        Thread.sleep(100);
        t1.interrupt();
        t2.interrupt();
        t3.interrupt();
        t4.interrupt();
        Thread.sleep(1000);
        semaphore.release();

    }
}
