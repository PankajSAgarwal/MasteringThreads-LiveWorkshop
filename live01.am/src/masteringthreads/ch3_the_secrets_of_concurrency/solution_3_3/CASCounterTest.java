package masteringthreads.ch3_the_secrets_of_concurrency.solution_3_3;

import masteringthreads.ch3_the_secrets_of_concurrency.exercise_3_3.*;

public class CASCounterTest {
    public static void main(String... args) throws InterruptedException {
        var cas = new CASCounter();
        var threads = new Thread[3];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int i1 = 0; i1 < 1000 * 1000; i1++) {
                    cas.increment();
                }
            });
            threads[i].start();
        }
        for (var thread : threads) {
            thread.join();
        }
        System.out.println("cas.getCount() = " + cas.getCount());
    }
}
