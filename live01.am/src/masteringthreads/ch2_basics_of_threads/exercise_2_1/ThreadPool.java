package masteringthreads.ch2_basics_of_threads.exercise_2_1;

import java.util.*;
import java.util.concurrent.atomic.*;

public class ThreadPool {
    private static final AtomicInteger nextThreadPoolNumber = new AtomicInteger(1);
    private final int threadPoolNumber = nextThreadPoolNumber.getAndIncrement();

    private final Object handlerMonitor = new Object();
    private Thread.UncaughtExceptionHandler handler = null;

    private final ThreadGroup group = new ThreadGroup("poolgroup-" + threadPoolNumber) {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            Thread.UncaughtExceptionHandler handler;
            synchronized (handlerMonitor) {
                handler = ThreadPool.this.handler;
            }
            if (handler != null) {
                handler.uncaughtException(t, e);
            } else super.uncaughtException(t, e);
        }
    };
    private final LinkedList<Runnable> tasks = new LinkedList<>();

    // Create a thread group field
    // Create a LinkedList field containing Runnable
    // Hint: Since LinkedList is not thread-safe, we need to synchronize it

    public ThreadPool(int poolSize) {
        // create several Worker threads in the thread group
        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker(group, "pool-" + threadPoolNumber + "-worker-" + (i + 1));
            worker.start();
        }
    }

    public void setHandler(Thread.UncaughtExceptionHandler handler) {
        synchronized (handlerMonitor) {
            this.handler = handler;
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
        // Add the job to the LinkedList and notifyAll
        synchronized (tasks) {
            tasks.add(job);
            tasks.notifyAll();
        }
    }

    public int getRunQueueLength() {
        synchronized (tasks) {
            return tasks.size();
        }
    }

    @SuppressWarnings("deprecation")
    public void shutdown() {
        // this should call stop() on the ThreadGroup.
        group.stop();
    }

    private class Worker extends Thread {
        public Worker(ThreadGroup group, String name) {
            super(group, name);
        }

        public void run() {
            while (true) {
                try {
                    Runnable job = take();
                    job.run();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted, and so??");
                }
            }
        }
    }
}
