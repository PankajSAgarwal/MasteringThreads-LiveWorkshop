package playground;

import java.lang.management.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class CpuTimes {
    public static void main(String... args) throws InterruptedException {
        test(() -> { // elapsed=1000, cpu=0, user=0
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Got it 1");
        });
        AtomicBoolean ready = new AtomicBoolean(false);
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ready.set(true);
            }
        }, 1000);
        test(() -> { // elapsed=1000, cpu=1000, user=1000
            while(!ready.get());
            System.out.println("Got it 2");
        });

        DelayQueue<DelayedDataImportJob> queue = new DelayQueue<>();
        queue.add(new DelayedDataImportJob());
        test(() -> { // elapsed=1000, cpu=1000, user=1000
            while(queue.poll() == null);
            System.out.println("Got it 3");
        });

        test(() -> { // elapsed=1000, cpu=1000, user=1000
            long end = System.nanoTime() + 1_000_000_000;
            while(System.nanoTime() < end);
            System.out.println("Got it 4");
        });
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setStop(true);
            }
        }, 1000);
        Runnable task = () -> {
            while (!isStop()) ;
        };
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();

        test(() -> { // elapsed=1000, cpu=1000, user=200
            task.run();
            System.out.println("Got it 5");
        });

    }
    private static boolean stop = false;

    public synchronized static boolean isStop() {
        return stop;
    }

    public synchronized static void setStop(boolean stop) {
        CpuTimes.stop = stop;
    }

    private static void test(Runnable job) {
        ThreadMXBean tmb = ManagementFactory.getThreadMXBean();
        long cpuTime = tmb.getCurrentThreadCpuTime();
        long userTime = tmb.getCurrentThreadUserTime();
        long elapsedTime = System.nanoTime();
        try {
            job.run();
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
    }

    private static class DelayedDataImportJob implements Delayed {
        private final LocalDateTime createTime = LocalDateTime.now();
        @Override
        public long getDelay(TimeUnit unit) {
            System.out.println("DelayedDataImportJob.getDelay");
            return unit.convert(LocalDateTime.now().until(createTime, ChronoUnit.MILLIS) + 1_000, TimeUnit.MILLISECONDS);
        }
        @Override
        public int compareTo(Delayed delayed) {
            return createTime.compareTo(((DelayedDataImportJob) delayed).createTime);
        }
    }
}
