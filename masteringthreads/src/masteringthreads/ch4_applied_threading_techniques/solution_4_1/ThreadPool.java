package masteringthreads.ch4_applied_threading_techniques.solution_4_1;

import java.util.concurrent.*;

// solution #3 - even better ...
public class ThreadPool {
    private final ThreadGroup group = new ThreadGroup("thread pool");
    private final BlockingQueue<Runnable> runQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    public ThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            var worker = new Worker(group, "Worker" + i);
            worker.start();
        }
    }

    private Runnable take() throws InterruptedException {
        return runQueue.take();
    }

    public void submit(Runnable job) {
        runQueue.add(job);
    }

    public int getRunQueueLength() {
        return runQueue.size();
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
            while (running && !isInterrupted()) {
                try {
                    take().run();
                } catch (InterruptedException e) {
                    interrupt();
                    break;
                }
            }
        }
    }
}
