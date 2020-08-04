package masteringthreads.ch4_applied_threading_techniques.solution_4_2;

import java.util.concurrent.*;

// solution #4 - the best ...
public class ThreadPool {
    private final ExecutorService pool;

    public ThreadPool(int poolSize) {
        pool = Executors.newFixedThreadPool(poolSize);
    }

    public void submit(Runnable job) {
        pool.submit(job);
    }

    public int getRunQueueLength() {
        return ((ThreadPoolExecutor) pool).getQueue().size();
    }

    public void shutdown() {
        pool.shutdownNow();
    }
}
