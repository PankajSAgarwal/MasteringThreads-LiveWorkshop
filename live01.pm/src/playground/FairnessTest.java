package playground;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class FairnessTest {
    public static final int PARTIES = 4;
    private static volatile boolean running = true;
    private static final Phaser phaser = new Phaser(PARTIES + 1);
    private static final Lock lock = new ReentrantLock(true);
    private static final StampedLock sl = new StampedLock();

    private static class Experiment extends Thread {
        private final LongAdder count = new LongAdder();

        @Override
        public void run() {
            phaser.arriveAndAwaitAdvance();
            while (running) {
                count.increment();
            }
            System.out.println("count = " + count);
        }
    }

    public static void main(String... args) throws InterruptedException {
        Experiment[] experiments = new Experiment[PARTIES];
        for (int i = 0; i < experiments.length; i++) {
            experiments[i] = new Experiment();
            experiments[i].start();
        }
        phaser.arriveAndDeregister();
        Thread.sleep(10_000);
        running = false;

    }
}
