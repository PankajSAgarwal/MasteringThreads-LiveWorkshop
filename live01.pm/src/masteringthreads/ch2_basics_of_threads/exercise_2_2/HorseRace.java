package masteringthreads.ch2_basics_of_threads.exercise_2_2;

import java.lang.management.*;

public class HorseRace {
    public static void main(String... args) throws InterruptedException {
        Runnable job = () -> loop(1_000_000_000);

        for (int i = 0; i < 10; i++) {
            long time = System.nanoTime();
            try {
                job.run();
            } finally {
                time = System.nanoTime() - time;
                System.out.printf("time = %dms%n", (time / 1_000_000));
            }
        }
        ThreadMXBean tmb = ManagementFactory.getThreadMXBean();
        Runnable race = () -> {
            long cpuTime = tmb.getCurrentThreadCpuTime();
            long userTime = tmb.getCurrentThreadUserTime();
            long elapsedTime = System.nanoTime();
            try {
                // HorseRace.class.wait()
                synchronized (HorseRace.class) {
                    try {
                        HorseRace.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("STARTED: " + Thread.currentThread());
                job.run();
                System.out.println("FINISHED: " + Thread.currentThread());
            } finally {
                elapsedTime = System.nanoTime() - elapsedTime;
                cpuTime = tmb.getCurrentThreadCpuTime() - cpuTime;
                userTime = tmb.getCurrentThreadUserTime() - userTime;
                System.out.printf("%d elapsedTime = %dms, cpuTime=%dms, userTime=%dms%n",
                    Thread.currentThread().getPriority(),
                    elapsedTime / 1_000_000,
                    cpuTime / 1_000_000,
                    userTime / 1_000_000);
            }
        };
        for (int priority = Thread.MIN_PRIORITY; priority <= Thread.MAX_PRIORITY; priority++) {
//            Thread.builder().priority(priority).task(race).start();
            Thread thread = new Thread(race);
            thread.setPriority(priority);
            thread.start();
        }
        System.out.println("on your marks ...");
        Thread.sleep(1000);
        System.out.println("get set ...");
        Thread.sleep(1000);
        System.out.println("GO!!!");
        synchronized (HorseRace.class) {
            HorseRace.class.notifyAll();
        }
    }

    public static long loop(long upto) {
        long total = 0;
        for (long i = 0; i < upto; i++) {
            total += i;
        }
        return total;
    }
}
