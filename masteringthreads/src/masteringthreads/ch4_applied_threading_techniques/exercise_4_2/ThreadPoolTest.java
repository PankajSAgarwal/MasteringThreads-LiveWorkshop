package masteringthreads.ch4_applied_threading_techniques.exercise_4_2;

import org.junit.*;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class ThreadPoolTest {
    @Test
    public void testTasksAreStopped() throws InterruptedException {
        var pool = new ThreadPool(1);
        var latch = new CountDownLatch(1);
        var time = System.currentTimeMillis();
        pool.submit(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        Thread.sleep(1000);
        pool.shutdown();
        boolean noTimeout = latch.await(100, TimeUnit.MILLISECONDS);
        assertTrue("timeout occurred - did not shutdown the threads in time?", noTimeout);
        time = System.currentTimeMillis() - time;
    }

    @Test
    public void testThatRunnablesAreExecutedConcurrently() throws InterruptedException {
        checkStandardThreadPoolFunctionality(new ThreadPool(10));
    }

    @Test
    public void testSynchronizingOnListObject() throws ReflectiveOperationException, InterruptedException {
        if (checkListAndBlockingQueue()) return;

        var pool = new ThreadPool(10);
        var list = findFieldValue(pool, List.class);
        synchronized (list) {
            var thread = new Thread(() -> {
                pool.submit(() -> System.out.println("submit worked"));
            });
            thread.start();
            thread.join(100);
            assertTrue("In submit(), we expected the pool to be synchronizing list access using the list object as a monitor lock", thread.isAlive());
        }
        synchronized (list) {
            var thread = new Thread(() -> {
                System.out.println("pool.getRunQueueLength() = " + pool.getRunQueueLength());
            });
            thread.start();
            thread.join(100);
            assertTrue("In getRunQueueLength(), we expected the pool to be synchronizing list access using the list object as a monitor lock", thread.isAlive());
        }
        pool.shutdown();
    }

    @Test
    public void testSpuriousWakeupsAreHandledCorrectly() throws InterruptedException, IllegalAccessException {
        if (checkListAndBlockingQueue()) return;


        var pool = new ThreadPool(10);
        Thread.sleep(100);
        var list = findFieldValue(pool, List.class);

        for (int i = 0; i < 20; i++) {
            synchronized (list) {
                list.notifyAll();
            }
        }
        checkStandardThreadPoolFunctionality(pool);
    }

    private boolean checkListAndBlockingQueue() {
        var foundExecutorField = false;
        var foundListField = false;
        var foundBlockingQueueField = false;
        for (var field : ThreadPool.class.getDeclaredFields()) {
            if (Executor.class.isAssignableFrom(field.getType())) {
                foundExecutorField = true;
            } else if (List.class.isAssignableFrom(field.getType())) {
                foundListField = true;
            } else if (BlockingQueue.class.isAssignableFrom(field.getType())) {
                foundBlockingQueueField = true;
            }
        }
        if (foundExecutorField) return true;
        if (foundBlockingQueueField && !foundListField) return true;
        if (foundBlockingQueueField && foundListField)
            fail("We don't need a List if we use a BlockingQueue");
        if (!foundListField)
            fail("We need a List field");
        return false;
    }

    private void checkStandardThreadPoolFunctionality(ThreadPool pool) throws InterruptedException {
        var latch = new CountDownLatch(19);
        var time = System.currentTimeMillis();
        for (int i = 0; i < 19; i++) {
            pool.submit(() -> {
                try {
                    Thread.sleep(1000);
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        boolean noTimeout = latch.await(3, TimeUnit.SECONDS);
        assertTrue("timeout occurred - did you start your threads?", noTimeout);
        time = System.currentTimeMillis() - time;
        pool.shutdown();
        if (pool.getRunQueueLength() != 0) {
            throw new AssertionError("Queue was not empty: "
                + pool.getRunQueueLength());
        }
        assertTrue("Total time exceeded limits", time < 2400);
        assertFalse("Faster than expected", time < 1900);
    }

    private <E> E findFieldValue(ThreadPool pool, Class<E> fieldType) throws IllegalAccessException {
        for (var field : pool.getClass().getDeclaredFields()) {
            if (fieldType.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                return fieldType.cast(field.get(pool));
            }
        }
        throw new IllegalArgumentException("Field of type " + fieldType + " not found");
    }

    private Thread interrupted = null;

    @Test
    public void testForBackupBoolean() throws InterruptedException {
        var latch = new CountDownLatch(8);
        var pool = new ThreadPool(10);
        for (int i = 0; i < 12; i++) {
            pool.submit(() -> {
                try {
                    Thread.sleep(1000);
                    latch.countDown();
                } catch (InterruptedException e) {
                    interrupted = Thread.currentThread();
                }
            });
        }
        boolean noTimeout = latch.await(2, TimeUnit.SECONDS);
        assertTrue("timeout occurred - did you start your threads?", noTimeout);
        pool.shutdown();
        Thread.sleep(100);
        assertTrue("Did you have a backup boolean?",
            interrupted == null || !interrupted.isAlive());
    }
}
