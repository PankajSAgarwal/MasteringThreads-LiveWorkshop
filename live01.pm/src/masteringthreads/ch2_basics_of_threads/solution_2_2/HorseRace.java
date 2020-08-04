package masteringthreads.ch2_basics_of_threads.solution_2_2;

public class HorseRace {
    public static final int LOOP_SIZE = 2000;

    public static void main(String... args) throws InterruptedException {
        Runnable race = () -> {
            try {
                synchronized (HorseRace.class) {
                    HorseRace.class.wait();
                }
            } catch (InterruptedException e) {
                // won't happen here ...
            }
            System.out.println("STARTED: " + Thread.currentThread());
            for (long i = 0; i < LOOP_SIZE; i++) {
                for (long j = 0; j < LOOP_SIZE; j++) {
                    for (long k = 0; k < LOOP_SIZE; k++) {
                    }
                }
            }
            System.out.println("FINISHED: " + Thread.currentThread());
        };
        for (int priority = Thread.MIN_PRIORITY; priority <= Thread.MAX_PRIORITY; priority++) {
            var thread = new Thread(race, "horse " + priority);
            thread.setPriority(priority);
            thread.start();
        }
        System.out.println("on your marks ...");
        Thread.sleep(1000);
        System.out.println("get set ...");
        Thread.sleep(1000);
        System.out.println("GO!!!");
        synchronized (HorseRace.class) {
            HorseRace.class.notifyAll();
        }
    }
}
