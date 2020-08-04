package playground;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class CpuTimes {
    public static void main(String[] args) {
        test(()->{// elapsed=1000,userTime=0,cpuTime=0
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        AtomicBoolean ready = new AtomicBoolean(false);
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ready.set(true);
            }
        },1000);

        test(()->{// elapsed=1000,userTime=1000,cpuTime=1000
            while(!ready.get());
            System.out.println("Got it");
        });

        test(()->{// elapsed=1000,userTime=1000,cpuTime=1000
            long end = System.nanoTime() + 1_000_000_000;
            while(System.nanoTime() < end);
            System.out.println("Got it");
        });

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setStop(true);
            }
        },1000);

        Runnable task = () -> {
            while (!isStop()) ;
        };
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();

        test(()->{// elapsed=1000,userTime=200,cpuTime=1000
            task.run();
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
        try{
            job.run();
        }finally{
            elapsedTime = System.nanoTime() - elapsedTime;
            cpuTime = tmb.getCurrentThreadCpuTime() - cpuTime;
            userTime = tmb.getCurrentThreadUserTime() - userTime;
            System.out.printf("%d elapsedTime = %dms, cpuTime=%dms, userTime=%dms%n",
                    Thread.currentThread().getPriority(),
                    elapsedTime/1_000_000,
                    cpuTime/1_000_000,
                    userTime/1_000_000);
        }
    }

}
