package playground;

import java.util.concurrent.locks.LockSupport;

public class ThreadCreation {
    public static void main(String[] args) {
        // create 30 threads and then observe top command
        for (int i = 0; i < 30; i++) {
            Thread thread = new Thread(()->{
                System.out.println("Started : " + Thread.currentThread());
                while(true);
                //LockSupport.park();// not active threads

            });
            thread.start();
        }
    }
}
