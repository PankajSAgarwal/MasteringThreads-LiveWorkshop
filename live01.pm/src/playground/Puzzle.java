package playground;

import java.util.concurrent.*;

public class Puzzle {
    public static double random() {
        return Math.random();
    }

    public static void main(String... args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            test();
        }
    }

    private static void test() throws InterruptedException {
        long time = System.nanoTime();
        try {
            Thread[] threads = new Thread[4];
            CountDownLatch latch = new CountDownLatch(threads.length);
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    double total = 0;
                    for (int j = 0; j < 10_000_000; j++) {
                        total += random();
                    }
                    System.out.println("total = " + total);
                    latch.countDown();
                });
                threads[i].start();
            }
            latch.await();
        } finally {
            time = System.nanoTime() - time;
            System.out.printf("time = %dms%n", (time / 1_000_000));
        }
    }
}
