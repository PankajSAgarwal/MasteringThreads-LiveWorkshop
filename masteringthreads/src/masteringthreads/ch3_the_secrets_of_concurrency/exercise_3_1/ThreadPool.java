package masteringthreads.ch3_the_secrets_of_concurrency.exercise_3_1;

import java.util.*;
import java.util.concurrent.locks.*;

public class ThreadPool {
    private final ThreadGroup group = new ThreadGroup("ThreadPoolGroup");
    private final List<Runnable> tasks = new LinkedList<>();

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

    @SuppressWarnings("deprecation")
    public void shutdown() {
        group.stop();
    }

    private class Worker extends Thread {
        public Worker(ThreadGroup group, String name) {
            super(group, name);
        }

        public void run() {
            while(true) {
                try {
                    take().run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
