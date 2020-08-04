package masteringthreads.ch5_threading_problems.exercise_5_1;

import java.lang.management.*;
import java.util.*;

public class PrinterClassReentrantLockTest {
    public static void main(String... args) {
        ThreadMXBean tmb = ManagementFactory.getThreadMXBean();
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long[] threads = tmb.findDeadlockedThreads();
                if (threads != null) {
                    System.out.println("Found the following threads in deadlock:");
                    for (long id : threads) {
                        ThreadInfo info = tmb.getThreadInfo(id);
                        System.out.println("\t" + info.getThreadName());
                    }
                    timer.cancel();
                    System.exit(1);
                }
            }
        }, 1000, 1000);
        PrinterClassReentrantLock pc = new PrinterClassReentrantLock();
        new Thread(() -> {
            while(true) pc.setPrintingEnabled(true);
        }, "setPrintingEnabled(true)").start();
        new Thread(() -> {
            while(true) pc.setPrintingEnabled(false);
        }, "setPrintingEnabled(false)").start();
        new Thread(() -> {
            while(true) pc.print("Hello world");
        }, "pc.print(\"Hello world\")").start();
        new Thread(() -> {
            while(true) pc.isPrintingEnabled();
        }, "isPrintingEnabled()").start();
    }
}
