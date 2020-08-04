package masteringthreads.ch5_threading_problems.solution_5_1;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

public class ThreadDeadlockDetector {
    private final Timer threadCheck = new Timer("ThreadDeadlockDetector", true);
    private final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
    private final Collection<Listener> listeners = new CopyOnWriteArraySet<>();

    /**
     * The number of milliseconds between checking for deadlocks.
     * It may be expensive to check for deadlocks, and it is not
     * critical to know so quickly.
     */
    private static final int DEFAULT_DEADLOCK_CHECK_PERIOD = 10000;

    public ThreadDeadlockDetector() {
        this(DEFAULT_DEADLOCK_CHECK_PERIOD);
    }

    public ThreadDeadlockDetector(int deadlockCheckPeriod) {
        threadCheck.schedule(new TimerTask() {
            public void run() {
                checkForDeadlocks();
            }
        }, 10, deadlockCheckPeriod);
    }

    private void checkForDeadlocks() {
        long[] ids = findDeadlockedThreads();
        if (ids != null && ids.length > 0) {
            var threads = new Thread[ids.length];
            Arrays.setAll(threads, i -> findMatchingThread(mbean.getThreadInfo(ids[i])));
            fireDeadlockDetected(threads);
        }
    }

    private long[] findDeadlockedThreads() {
        return mbean.findDeadlockedThreads();
    }

    private void fireDeadlockDetected(Thread[] threads) {
        listeners.forEach(l -> l.deadlockDetected(threads));
    }

    private Thread findMatchingThread(ThreadInfo inf) {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getId() == inf.getThreadId()) {
                return thread;
            }
        }
        throw new IllegalStateException("Deadlocked Thread not found");
    }

    public boolean addListener(Listener l) {
        return listeners.add(l);
    }

    public boolean removeListener(Listener l) {
        return listeners.remove(l);
    }

    /**
     * This is called whenever a problem with threads is detected.
     */
    public interface Listener {
        void deadlockDetected(Thread[] deadlockedThreads);
    }
}
