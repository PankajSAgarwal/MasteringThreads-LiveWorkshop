package playground;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

public class ThreadCreationDemo {
    public static void main(String... args) throws InterruptedException {
        ThreadMXBean tmb = ManagementFactory.getThreadMXBean();
        int peakThreadCount = tmb.getPeakThreadCount();
        System.out.println("peakThreadCount = " + peakThreadCount);
        test();
        peakThreadCount = tmb.getPeakThreadCount() - peakThreadCount;
        System.out.println("peakThreadCount diff = " + peakThreadCount);
        for (int i = 0; i < 100; i++) {
            test();
        }
    }

    private static void test() throws InterruptedException {
        long time = System.nanoTime();
        try {
            Thread[] threads = new Thread[8000];
            Phaser phaser = new Phaser(threads.length);
            Runnable empty = () -> {
                phaser.arriveAndAwaitAdvance();
            };
            for (int i = 0; i < threads.length; i++) {
                threads[i] = Thread.startVirtualThread(empty);
            }
            for (Thread thread : threads) {
                thread.join();
            }
        } finally {
            time = System.nanoTime() - time;
            System.out.printf("time = %dms%n", (time / 1_000_000));
        }
    }
}
