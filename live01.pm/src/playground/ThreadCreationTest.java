package playground;

import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class ThreadCreationTest {
    public static void main(String... args) {
        var threads_created = new AtomicInteger(0);
        while (true) {
            Runnable job = () -> {
                System.out.println("threads created: " +
                    threads_created.incrementAndGet());
                LockSupport.park();
            };
            new Thread(job).start();
        }
    }
}
