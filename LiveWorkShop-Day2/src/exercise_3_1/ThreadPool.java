package exercise_3_1;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.*;

public class ThreadPool {
    private final ThreadGroup group = new ThreadGroup("ThreadPoolGroup");
    private final List<Runnable> tasks = new LinkedList<>();
    // private volatile boolean running = true;
    private final AtomicBoolean running = new AtomicBoolean(true); // backup boolean , instead of AtomicBoolean , volatile boolean field can also be used.

    public ThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            var worker = new Worker(group, "worker-" + i);
            worker.start();
        }
    }

    private Runnable take() throws InterruptedException {
        if(Thread.interrupted()) throw new InterruptedException();
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

        running.set(false);
        group.interrupt();// group.stop() is deprecated but not group.interrupt
    }

    private class Worker extends Thread {
        public Worker(ThreadGroup group, String name) {
            super(group, name);
        }

        public void run() {
            while(running.get()) {
                try {
                    take().run();
                } catch (InterruptedException e) {
                    break;
                    //e.printStackTrace();
                }
            }
        }
    }
}
