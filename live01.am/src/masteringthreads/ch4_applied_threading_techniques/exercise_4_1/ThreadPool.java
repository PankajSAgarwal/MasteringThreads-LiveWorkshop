package masteringthreads.ch4_applied_threading_techniques.exercise_4_1;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

// TODO: Replace List with LinkedBlockingQueue
public class ThreadPool {
    private final ThreadGroup group = new ThreadGroup("ThreadPoolGroup");
    private final AtomicInteger outstandingTasks = new AtomicInteger();
    private final BlockingQueue<Runnable> tasks = new LinkedTransferQueue<>();
    private boolean running = true;

    public ThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            var worker = new Worker(group, "worker-" + i);
            worker.start();
        }
    }

    private Runnable take() throws InterruptedException {
        try {
            return tasks.take();
        } finally {
            outstandingTasks.decrementAndGet();
        }
    }

    public void submit(Runnable job) {
        outstandingTasks.incrementAndGet();
        tasks.add(job);
//        tasks.offer(job);
//        tasks.put(job);
    }

    public int getRunQueueLength() {
        return tasks.size();
    }

    public void shutdown() {
        running = false;
        group.interrupt();
    }

    private class Worker extends Thread {
        public Worker(ThreadGroup group, String name) {
            super(group, name);
        }

        public void run() {
            while (running) {
                try {
                    take().run();
                } catch (InterruptedException consumeAndExit) {
                    return;
                }
            }
        }
    }

}
