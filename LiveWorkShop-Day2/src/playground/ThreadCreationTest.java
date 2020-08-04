package playground;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This program will crash the system.
 * It is used to show how many threads your OS can support
 * On Mac OS with intel core i5 2 core 1 processor ,
 * it is able to create 2023 threads
 * before crashing
 */
public class ThreadCreationTest {

    public static void main(String[] args) {
        var threads_created = new AtomicInteger(0);

        while(true){
            new Thread(){
                @Override
                public void run() {
                    System.out.println("threads created: " +threads_created.incrementAndGet());
                    synchronized (this){
                        try{wait();}
                        catch (InterruptedException e){
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }.start();
        }
    }
}
