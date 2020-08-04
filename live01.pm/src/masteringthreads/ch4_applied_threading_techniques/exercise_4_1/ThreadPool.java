package masteringthreads.ch4_applied_threading_techniques.exercise_4_1;

import java.util.concurrent.*;

public class ThreadPool {
    private final ThreadGroup group = new ThreadGroup("ThreadPoolGroup");
    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
    private final RejectedTaskHandler handler;
    private boolean running = true;

    public ThreadPool(int poolSize, RejectedTaskHandler handler) {
        this.handler = handler;
        for (int i = 0; i < poolSize; i++) {
            var worker = new Worker(group, "worker-" + i);
            worker.start();
        }
    }

    public ThreadPool(int poolSize) {
        this(poolSize, new AbortPolicy());
    }

    private Runnable take() throws InterruptedException {
        return tasks.take();
    }

    public void submit(Runnable job) {
        if (!running) throw new RejectedExecutionException();
        boolean success = tasks.offer(job);
        if (!success) handler.rejectedTask(job);
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

    public static interface RejectedTaskHandler {
        void rejectedTask(Runnable r);
    }

    public static class AbortPolicy implements RejectedTaskHandler {
        @Override
        public void rejectedTask(Runnable r) {
            throw new RejectedExecutionException();
        }
    }

    public static class CallerRunsPolicy implements RejectedTaskHandler {
        @Override
        public void rejectedTask(Runnable r) {
            r.run();
        }
    }
}
