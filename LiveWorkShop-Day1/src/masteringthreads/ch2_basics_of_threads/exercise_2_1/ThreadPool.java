package masteringthreads.ch2_basics_of_threads.exercise_2_1;

import java.util.LinkedList;
import java.util.Objects;


public class ThreadPool {
    private static int nextThreadPoolNumber = 1;
    private final int threadPoolNumber;
    {
        synchronized (ThreadPool.class){
            threadPoolNumber = nextThreadPoolNumber++;
        }
    }
    private final Object handlerMonitor = new Object();
    private Thread.UncaughtExceptionHandler handler = null;
    // Create a thread group field
    private final ThreadGroup group = new ThreadGroup("poolGroup-"+threadPoolNumber){
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            Thread.UncaughtExceptionHandler handler;
            synchronized (handlerMonitor){
                handler = ThreadPool.this.handler;
            }
            if(handler != null)
                handler.uncaughtException(t,e);
            else
                super.uncaughtException(t, e);
        }
    };

    // Create a LinkedList field containing Runnable
    // Hint: Since LinkedList is not thread-safe, we need to synchronize it

    private final LinkedList<Runnable> tasks = new LinkedList<>();

    public ThreadPool(int poolSize) {
        // create several Worker threads in the thread group
        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker(group,"pool-"+threadPoolNumber +"-worker-"+(i+1));
            worker.start();
        }

    }

    public void setHandler(Thread.UncaughtExceptionHandler handler){
        synchronized (handlerMonitor){
            this.handler = handler;
        }
    }

    private Runnable take() throws InterruptedException {
        // if the LinkedList is empty, we wait
        //
        // remove the first job from the LinkedList and return it
        if(Thread.interrupted()) throw new InterruptedException();
        synchronized (tasks){
            while (tasks.isEmpty())
                tasks.wait();
            return tasks.removeFirst();
        }
    }

    public void submit(Runnable job) {
        // Add the job to the LinkedList and notifyAll
        Objects.requireNonNull(job,"job==null");
        synchronized (tasks){
            tasks.add(job);
            tasks.notifyAll();
        }
    }

    public int getRunQueueLength() {
        // return the length of the LinkedList
        // remember to also synchronize!
        synchronized (tasks){
            return tasks.size();
        }
    }

    @SuppressWarnings("deprecation")
    public void shutdown() {
        // this should call stop() on the ThreadGroup.
        group.stop();// stop is deprecated , don't use in production code
    }

    private class Worker extends Thread {
        public Worker(ThreadGroup group, String name) {
            super(group, name);
        }

        public void run() {
            // we run in an infinite loop:
            // remove the next job from the linked list using take()
            // we then call the run() method on the job
            while (true){
                Runnable job = null;
                try {
                    job = take();
                    job.run();
                } catch (InterruptedException e) {
                    System.err.println("Interrupted , and so ??");
                }
            }
        }
    }
}
