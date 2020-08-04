package masteringthreads.ch5_threading_problems.solution_5_1;

public class DefaultDeadlockListener implements ThreadDeadlockDetector.Listener {
    public void deadlockDetected(Thread[] threads) {
        System.err.println("Deadlocked Threads:");
        System.err.println("-------------------");
        for (var thread : threads) {
            System.err.println(thread);
            for (StackTraceElement ste : thread.getStackTrace()) {
                System.err.println("\t" + ste);
            }
        }
    }
}
