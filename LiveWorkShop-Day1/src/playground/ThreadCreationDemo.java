package playground;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.concurrent.Phaser;

public class ThreadCreationDemo {
    public static void main(String[] args) throws InterruptedException {
        ThreadMXBean tmb = ManagementFactory.getThreadMXBean();
        int peakThreadCount = tmb.getPeakThreadCount(); // when jvm starts there already be many threads like GC thread etc
        System.out.println("peakThreadCount = " + peakThreadCount);
        test();
        peakThreadCount =  tmb.getPeakThreadCount() - peakThreadCount;
        System.out.println("peakThreadCount diff = " + peakThreadCount);
        for (int i = 0; i < 10; i++) {
            test();
        }

    }

    private static void test() throws InterruptedException {
        long time = System.nanoTime();

        try {
            Thread[] threads = new Thread[2000];
            Phaser phaser = new Phaser(threads.length);
            Runnable empty = ()->{
                phaser.arriveAndAwaitAdvance();
            };

            // starting threads in parallel

            Arrays.parallelSetAll(threads,index ->{
                Thread thread = new Thread(empty);
                thread.start();
                return thread;
            });

            // sequential starting of thread
            /*for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(empty);
                threads[i].start();
            }*/
            for (Thread thread : threads) {
                thread.join();
            }
        } finally {
            time = System.nanoTime() - time;
            System.out.printf("time = %dms%n" ,(time/1_000_000));
        }
    }
}
