package playground;

import java.util.concurrent.locks.*;

public class ThreadCreation {
    public static void main(String... args) {
        for (int i = 0; i < 30; i++) {
            Thread thread = new Thread(() -> {
                System.out.println("Started " + Thread.currentThread());
//                while(true);
                LockSupport.park();
            });
            thread.start();
        }
    }
}
