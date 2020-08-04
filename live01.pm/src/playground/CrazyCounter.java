package playground;

import java.util.*;

public class CrazyCounter {
    private static volatile long counter = 0;
    private static class CompositeRunnable implements Runnable {
        private final Runnable[] runnables;

        public CompositeRunnable(Runnable... runnables) {
            this.runnables = runnables;
        }

        @Override
        public void run() {
            for (Runnable runnable : runnables) {
                runnable.run();
            }
        }
    }
    public static void main(String... args) {

        Runtime.getRuntime().addShutdownHook(new Thread(new CompositeRunnable(
            () -> {
                System.out.printf(Locale.US,  "1 counter = %,d%n", counter);
            },
            () -> {
                System.out.printf(Locale.US,  "2 counter = %,d%n", counter);
            },
            () -> {
                System.out.printf(Locale.US,  "3 counter = %,d%n", counter);
            }
        )));


        while(true) counter++;
    }
}
