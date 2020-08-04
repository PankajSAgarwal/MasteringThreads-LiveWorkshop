package masteringthreads.ch2_basics_of_threads.exercise_2_1;

public class ThreadPool {
    // Create a thread group field
    // Create a LinkedList field containing Runnable
    // Hint: Since LinkedList is not thread-safe, we need to synchronize it

    public ThreadPool(int poolSize) {
        // create several Worker threads in the thread group
    }

    private Runnable take() throws InterruptedException {
        // if the LinkedList is empty, we wait
        //
        // remove the first job from the LinkedList and return it
        throw new UnsupportedOperationException("not implemented");
    }

    public void submit(Runnable job) {
        // Add the job to the LinkedList and notifyAll
    }

    public int getRunQueueLength() {
        // return the length of the LinkedList
        // remember to also synchronize!
        throw new UnsupportedOperationException("not implemented");
    }

    @SuppressWarnings("deprecation")
    public void shutdown() {
        // this should call stop() on the ThreadGroup.
    }

    private class Worker extends Thread {
        public Worker(ThreadGroup group, String name) {
            super(group, name);
        }

        public void run() {
            // we run in an infinite loop:
            // remove the next job from the linked list using take()
            // we then call the run() method on the job
        }
    }
}
