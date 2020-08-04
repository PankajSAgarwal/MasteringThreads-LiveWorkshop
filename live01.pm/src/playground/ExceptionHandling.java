package playground;

import java.util.concurrent.*;

public class ExceptionHandling {
    public static void main(String... args) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.err.println("Oh no, " + t + " has thrown " + e);
            }
        });
        Runnable task = () -> {
            throw new IllegalStateException("blaaa");
        };
        new Thread(task).start();

        ExecutorService pool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                if (t == null
                    && r instanceof Future<?>
                    && ((Future<?>) r).isDone()) {
                    try {
                        Object result = ((Future<?>) r).get();
                    } catch (CancellationException ce) {
                        t = ce;
                    } catch (ExecutionException ee) {
                        t = ee.getCause();
                    } catch (InterruptedException ie) {
                        // ignore/reset
                        Thread.currentThread().interrupt();
                    }
                }
                if (t != null)
                    System.err.println("Oops, task " + r + " has thrown " + t);
            }
        };
        Future<?> future = pool.submit(task);

    }
}
