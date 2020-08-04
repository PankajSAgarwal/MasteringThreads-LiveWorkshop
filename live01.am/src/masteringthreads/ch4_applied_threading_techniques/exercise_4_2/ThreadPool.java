package masteringthreads.ch4_applied_threading_techniques.exercise_4_2;

import java.util.concurrent.*;

public class ThreadPool {
    private final ThreadPoolExecutor pool;

    public ThreadPool(int poolSize) {
        pool = new ThreadPoolExecutor(poolSize, poolSize,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());
    }

    public void submit(Runnable job) {
        pool.execute(job);
    }

    public int getRunQueueLength() {
        return pool.getQueue().size();
    }

    public void shutdown() {
        pool.shutdownNow();
    }
}
