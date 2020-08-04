package playground;

import java.util.concurrent.*;

public class ThreadPoolStartTest {
    public static void main(String... args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            test();
        }
    }

    private static void test() throws InterruptedException {
        long time = System.nanoTime();
        try {
            ExecutorService pool = Executors.newSingleThreadExecutor();
            ((ThreadPoolExecutor) pool).setMaximumPoolSize(8000);
            ((ThreadPoolExecutor) pool).setCorePoolSize(8000);
            ((ThreadPoolExecutor) pool).prestartAllCoreThreads();
            pool.shutdown();
            pool.awaitTermination(1, TimeUnit.SECONDS);
        } finally {
            time = System.nanoTime() - time;
            System.out.printf("time = %dms%n", (time / 1_000_000));
        }
    }
}
