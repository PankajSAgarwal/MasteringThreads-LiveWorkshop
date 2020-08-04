package masteringthreads.ch4_applied_threading_techniques.exercise_4_1;

import java.util.*;
import java.util.concurrent.locks.*;

// TODO: Replace List with LinkedBlockingQueue
public class ThreadPool {
    private final ThreadGroup group = new ThreadGroup("ThreadPoolGroup");
    private final List<Runnable> tasks = new LinkedList<>();
    private boolean running = true;

    public ThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            var worker = new Worker(group, "worker-" + i);
            worker.start();
        }
    }

    private Runnable take() throws InterruptedException {
        synchronized (tasks) {
            while (tasks.isEmpty()) tasks.wait();
            return tasks.remove(0);
        }
    }

    public void submit(Runnable job) {
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
