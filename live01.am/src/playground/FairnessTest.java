package playground;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class FairnessTest {
    public static final int PARTIES = 4;
    private static volatile boolean running = true;
    private static final Phaser phaser = new Phaser(PARTIES + 1);
    private static final Lock lock = new ReentrantLock(true);

    private static class Experiment extends Thread {
        private long count;

        @Override
        public void run() {
            phaser.arriveAndAwaitAdvance();
            while (running) {
                lock.lock();
                try {
                    count++;
                } finally {
                    lock.unlock();
                }
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
