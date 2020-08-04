package masteringthreads.ch3_the_secrets_of_concurrency.exercise_3_3;

import java.util.concurrent.atomic.*;

// DO NOT CHANGE
public class CASCounter implements Counter {
    private volatile long count = 0;
    private final AtomicReference<Thread> owner = new AtomicReference<>(null);

    public long getCount() {
        return count;
    }

    public void increment() {
//        Thread current, next = Thread.currentThread();
//        do {
//            current = owner.get();
//        } while(current != null || !owner.compareAndSet(current, next));
//         always only one thread
        this.count++;
//        this.owner.set(null);
    }
}
