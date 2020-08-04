package playground;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class HaberdasheryDemo {
    public static void main(String... args) {
        BlockingQueue<Integer> meaningOfLife = new LinkedBlockingQueue<>();
        long time = System.nanoTime();
        try {
            var threads_created = new AtomicInteger(0);
            Thread.Builder builder = Thread.builder().virtual().task(() -> {
                System.out.println("threads created: " +
                    threads_created.incrementAndGet());
                try {
                    Thread.sleep(1000_0000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            while(true) {
                builder.start();
            }
        } finally {
            time = System.nanoTime() - time;
            System.out.printf("time = %dms%n", (time / 1_000_000));
        }
    }
}
