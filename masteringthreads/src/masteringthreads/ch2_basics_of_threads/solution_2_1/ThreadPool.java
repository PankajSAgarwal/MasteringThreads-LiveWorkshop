package masteringthreads.ch2_basics_of_threads.solution_2_1;

import java.util.*;

// solution_2_1 #1 - not perfect yet ...
public class ThreadPool {
    // Create a thread group field
    private final ThreadGroup group = new ThreadGroup("ThreadPoolGroup");
    // Create a LinkedList field containing Runnable
    private final List<Runnable> tasks = new LinkedList<>();

    public ThreadPool(int poolSize) {
        // create several Worker threads in the thread group
        for (int i = 0; i < poolSize; i++) {
            var worker = new Worker(group, "worker-" + i);
            worker.start();
        }
    }

    private Runnable take() throws InterruptedException {
        synchronized (tasks) {
            // if the LinkedList is empty, we wait
            while (tasks.isEmpty()) tasks.wait();
            // remove the first job from the LinkedList and return it
            return tasks.remove(0);
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
        // return the length of the LinkedList
        // remember to also synchronize!
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
            // we run in an infinite loop:
            while(true) {
                // remove the next job from the linked list using take()
                // we then call the run() method on the job
                try {
                    take().run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
