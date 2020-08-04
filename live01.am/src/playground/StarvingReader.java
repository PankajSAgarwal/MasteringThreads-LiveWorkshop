package playground;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class StarvingReader {
    private volatile static boolean running = true;
    private static long writes;
    private static long reads;

    /*
    ReentrantLock
    writes = 329,464,863
    reads = 31,101

    ReentrantLock fair
    writes = 1,123,083
    reads = 35,078

    StampedLock
    writes = 292,989,060
    reads = 5,080,973

    StampedLock with Optimistic Read
    writes = 84,704,974
    reads = 886,912,101
     */
    public static void main(String... args) throws InterruptedException {
        StampedLock sl = new StampedLock();

        for (int i = 0; i < 32; i++) {
            new Thread(() -> {
                while (running) {
                    long stamp = sl.writeLock();
                    try {
                        writes++;
                    } finally {
                        sl.unlockWrite(stamp);
                    }
                }
            }, "writer-" + i).start();
        }
        new Thread(() -> {
            while (running) {
                long stamp = sl.tryOptimisticRead();
                if (sl.validate(stamp))
                    reads++;
            }
        }, "reader").start();
        Thread.sleep(10_000);
        running = false;
        Thread.sleep(100);
        System.out.println("writes = " + writes);
        System.out.println("reads = " + reads);
    }
}
