package playground;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.atomic.*;

public class BankAccount {
    private final AtomicInteger concurrentCalls = new AtomicInteger();

    private static class BalanceLock {}
    private static class AddressLock {}
    private final Object balanceLock = new BalanceLock();
    private final Object addressLock = new AddressLock();
    private double balance;
    private final List<String> address = new ArrayList<>();

    public void deposit(double amount) {
        synchronized (balanceLock) {
            balance += amount;
            int concurrent = concurrentCalls.incrementAndGet();
//            if (concurrent > 1) System.out.println("concurrent = " + concurrent + " " + Thread.currentThread().getId());
            concurrentCalls.decrementAndGet();
        }
    }

    public void printAddress() {
        synchronized (addressLock) {
            for (String line : address) {
                System.out.println(line);
            }
            int concurrent = concurrentCalls.incrementAndGet();
//            if (concurrent > 1) System.out.println("concurrent = " + concurrent + " " + Thread.currentThread().getId());
            concurrentCalls.decrementAndGet();
        }
    }

    public void setAddress(String... address) {
        synchronized (addressLock) {
            this.address.clear();
            for (String line : address) {
                this.address.add(line);
            }
            int concurrent = concurrentCalls.incrementAndGet();
//            if (concurrent > 1) System.out.println("concurrent = " + concurrent + " " + Thread.currentThread().getId());
            concurrentCalls.decrementAndGet();
        }
    }

    public static void main(String... args) {
        BankAccount account = new BankAccount();

        ThreadMXBean tmb = ManagementFactory.getThreadMXBean();
//        tmb.setThreadContentionMonitoringEnabled(true);

        new Thread(() -> {
            for (int i = 0; i < 1000_000_000; i++) {
                account.setAddress("Heinz Kabutz", "Chorafakia", "Greece");
            }
            ThreadInfo info = tmb.getThreadInfo(Thread.currentThread().getId());
            System.out.println("address info.getBlockedCount() = " + info.getBlockedCount());
            System.out.println("address info.getBlockedTime() = " + info.getBlockedTime());
        }, "address thread").start();

        new Thread(() -> {
            for (int i = 0; i < 1000_000_000; i++) {
                account.deposit(1000_000_000);
            }
            ThreadInfo info = tmb.getThreadInfo(Thread.currentThread().getId());
            System.out.println("balance1 info.getBlockedCount() = " + info.getBlockedCount());
            System.out.println("balance1 info.getBlockedTime() = " + info.getBlockedTime());
        }, "balance1 thread").start();
        new Thread(() -> {
            for (int i = 0; i < 1000_000_000; i++) {
                account.deposit(100);
            }
            ThreadInfo info = tmb.getThreadInfo(Thread.currentThread().getId());
            System.out.println("balance2 info.getBlockedCount() = " + info.getBlockedCount());
            System.out.println("balance2 info.getBlockedTime() = " + info.getBlockedTime());
        }, "balance2 thread").start();
    }
}
