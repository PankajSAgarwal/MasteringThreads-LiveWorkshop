package playground;

import java.lang.management.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class DeadlockExample {
    public static void main(String... args) throws InterruptedException {

        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        lock(lock1, lock2);
        lock(lock2, lock1);
        Thread.sleep(500);
        checkForDeadlock();  // true
        Thread.sleep(6000);
        checkForDeadlock(); // has to be true too
    }

    private static void lock(Lock lock1, Lock lock2) throws InterruptedException {
        new Thread(() -> {
            try {
                lock1.lock();
                try {
                    Thread.sleep(100);
                    if (lock2.tryLock(5, TimeUnit.SECONDS)) {
                        try {
                            System.out.println("Got both locks");
                        } finally {
                            lock2.unlock();
                        }
                    }
                } finally {
                    lock1.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void checkForDeadlock() {
        ThreadMXBean tmb = ManagementFactory.getThreadMXBean();
        long[] threads = tmb.findDeadlockedThreads();
        if (threads != null) {
            System.out.println("Found the following threads in deadlock:");
            for (long id : threads) {
                ThreadInfo info = tmb.getThreadInfo(id);
                System.out.println("\t" + info.getThreadName());
            }
        } else {
            System.out.println("No deadlock");
        }
    }
}
