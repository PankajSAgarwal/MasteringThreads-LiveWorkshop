package masteringthreads.ch2_basics_of_threads.exercise_2_1;

import net.jcip.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class ThreadPool {
    private final ThreadGroup group = new ThreadGroup("poolgroup");
    // guarded by "tasks"
    @GuardedBy("tasks")
    private final LinkedList<Runnable> tasks = new LinkedList<>();

    public ThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            new Worker(group, "worker-" + (i + 1)).start();
        }
    }

    private Runnable take() throws InterruptedException {
        if (Thread.interrupted()) throw new InterruptedException();
        synchronized (tasks) {
            while (tasks.isEmpty()) tasks.wait();
            return tasks.removeFirst();
        }
    }

    public void submit(Runnable job) {
        Objects.requireNonNull(job, "job==null");
        synchronized (tasks) {
            tasks.add(job);
            tasks.notifyAll();
        }
    }

    public <T> Future<T> submit(Callable<T> job) {
        Objects.requireNonNull(job);
        FutureTask<T> futureTask = new FutureTask<>(job);
        synchronized (tasks) {
            tasks.add(futureTask);
            tasks.notifyAll();
        }
        return futureTask;
    }


    public int getRunQueueLength() {
        synchronized (tasks) {
            return tasks.size();
        }
    }

    @SuppressWarnings("deprecation")
    public void shutdown() {
        group.stop();
        while (group.activeCount() != 0) {
            System.err.println("Waiting for pool to shut down");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Worker extends Thread {
        public Worker(ThreadGroup group, String name) {
            super(group, name);
        }

        public void run() {
            try {
                // we run in an infinite loop:
                while (true) {
                    take().run();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.err.println("Say goodbye to " + this);
            }
        }
    }
}
