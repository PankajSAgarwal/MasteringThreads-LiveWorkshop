package playground;

import java.util.LinkedList;
import java.util.List;

public class ProducerConsumer {
    private final List<Runnable> tasks = new LinkedList<>();

    public ProducerConsumer() {
        var thread = new Thread(()->{
           try{
               while (true)
                   take().run();
           }catch (InterruptedException ignore){ }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private Runnable take() throws InterruptedException{
        synchronized (tasks){
            while(tasks.isEmpty())
                tasks.wait();
            return tasks.remove(0);
        }
    }
    public void submit(Runnable task){
        synchronized (tasks){
            tasks.add(task);
            tasks.notifyAll();
        }


    }
}
