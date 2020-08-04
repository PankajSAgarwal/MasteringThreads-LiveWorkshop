package masteringthreads.ch5_threading_problems.exercise_5_1;

import java.util.concurrent.*;

public class PrinterTest {
    private volatile static boolean running = true;

    public static void main(String... args) throws InterruptedException {
        PrinterClass pc = new PrinterClass();
        ExecutorService pool = Executors.newCachedThreadPool(
            new ThreadFactory() {
                private final ThreadFactory defaultFactory =
                    Executors.defaultThreadFactory();
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = defaultFactory.newThread(r);
                    t.setName("PrinterTest-" + t.getName());
                    return t;
                }
            });
        pool.submit(() -> {
            try (var restorer = new NameRestorer("print(\"Hello world\")")) {
                while (running) {
                    pc.print("Hello world");
                }
            }
        });
        pool.submit(() -> {
            try (var restorer = new NameRestorer("setPrintingEnabled(true)")) {
                while (running) {
                    pc.setPrintingEnabled(true);
                }
            }
        });
        pool.submit(() -> {
            try (var restorer = new NameRestorer("isPrintingEnabled()")) {
                while (running) {
                    pc.isPrintingEnabled();
                }
            }
        });
        pool.submit(() -> {
            try (var restorer = new NameRestorer("setPrintingEnabled(false)")) {
                while (running) {
                    pc.setPrintingEnabled(false);
                }
            }
        });

        Thread.sleep(3000);
        running = false;
        pool.shutdown();
        if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("Suspected Deadlock");
//            System.exit(1);
        } else {
            System.out.println("All good, no deadlock");
        }
    }

    private static class NameRestorer implements AutoCloseable {
        private final String oldThreadName;

        public NameRestorer(String newThreadName) {
            this.oldThreadName = Thread.currentThread().getName();
            Thread.currentThread().setName(oldThreadName + "#" + newThreadName);
        }

        @Override
        public void close() {
            Thread.currentThread().setName(oldThreadName);
        }
    }
}
