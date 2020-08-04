package masteringthreads.ch5_threading_problems.solution_5_1;

import masteringthreads.ch5_threading_problems.exercise_5_1.*;

public class PrinterClassAutomaticDetectionTest {
    public static void main(String... args) {
        var detector = new ThreadDeadlockDetector(500);
        detector.addListener(new DefaultDeadlockListener() {
            public void deadlockDetected(Thread[] threads) {
                super.deadlockDetected(threads);
                System.exit(1);
            }
        });

        var pc = new PrinterClass();
        new Thread() {
            {
                start();
            }

            public void run() {
                for (int i = 0; i < 10000000; i++) {
                    pc.print("testing");
                }
            }
        };
        new Thread() {
            {
                start();
            }

            public void run() {
                for (int i = 0; i < 10000000; i++) {
                    pc.setPrintingEnabled(false);
                }
            }
        };
    }
}
