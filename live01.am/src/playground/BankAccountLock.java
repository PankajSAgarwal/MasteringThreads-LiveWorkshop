package playground;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class BankAccountLock {
    private final AtomicInteger concurrentCalls = new AtomicInteger();

    private final ReentrantLock balanceLock = new ReentrantLock();
    private double balance;

    private final ReentrantLock addressLock = new ReentrantLock();
    private final List<String> address = new ArrayList<>();

    public void deposit(double amount) {
        balanceLock.lock();
        try {
            balance += amount;
            int concurrent = concurrentCalls.incrementAndGet();
            //            if (concurrent > 1) System.out.println("concurrent = " + concurrent + " " + Thread.currentThread().getId());
            concurrentCalls.decrementAndGet();
        } finally {
            balanceLock.unlock();
        }
    }

    public void printAddress() {
        addressLock.lock();
        try {
            for (String line : address) {
                System.out.println(line);
            }
            int concurrent = concurrentCalls.incrementAndGet();
            //            if (concurrent > 1) System.out.println("concurrent = " + concurrent + " " + Thread.currentThread().getId());
            concurrentCalls.decrementAndGet();
        } finally {
            addressLock.unlock();
        }
    }

    public void setAddress(String... address) {
        addressLock.lock();
        try {
            this.address.clear();
            for (String line : address) {
                this.address.add(line);
            }
            int concurrent = concurrentCalls.incrementAndGet();
            //            if (concurrent > 1) System.out.println("concurrent = " + concurrent + " " + Thread.currentThread().getId());
            concurrentCalls.decrementAndGet();
        } finally {
            addressLock.unlock();
        }
    }

    public static void main(String... args) {
        BankAccountLock account = new BankAccountLock();

        ThreadMXBean tmb = ManagementFactory.getThreadMXBean();
        tmb.setThreadContentionMonitoringEnabled(true);

        new Thread(() -> {
            for (int i = 0; i < 10_000_000; i++) {
                account.setAddress("Heinz Kabutz", "Chorafakia", "Greece");
            }
            ThreadInfo info = tmb.getThreadInfo(Thread.currentThread().getId());
            System.out.println("address info.getBlockedCount() = " + info.getBlockedCount());
            System.out.println("address info.getBlockedTime() = " + info.getBlockedTime());
        }, "address thread").start();

        new Thread(() -> {
            for (int i = 0; i < 10_000_000; i++) {
                account.deposit(100_000_000);
            }
            ThreadInfo info = tmb.getThreadInfo(Thread.currentThread().getId());
            System.out.println("balance1 info.getBlockedCount() = " + info.getBlockedCount());
            System.out.println("balance1 info.getBlockedTime() = " + info.getBlockedTime());
        }, "balance1 thread").start();
        new Thread(() -> {
            for (int i = 0; i < 10_000_000; i++) {
                account.deposit(100);
            }
            ThreadInfo info = tmb.getThreadInfo(Thread.currentThread().getId());
            System.out.println("balance2 info.getBlockedCount() = " + info.getBlockedCount());
            System.out.println("balance2 info.getBlockedTime() = " + info.getBlockedTime());
        }, "balance2 thread").start();
    }
}
