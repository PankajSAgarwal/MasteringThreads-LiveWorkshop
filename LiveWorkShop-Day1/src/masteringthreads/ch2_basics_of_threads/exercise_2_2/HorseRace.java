package masteringthreads.ch2_basics_of_threads.exercise_2_2;

public class HorseRace {
    public static void main(String... args) throws InterruptedException {
        for (int i = 0; i < 30; i++) {
            test();
        }
        Runnable race = () -> {
            // todo: wait on a common lock object, e.g.
            // HorseRace.class.wait()
            synchronized (HorseRace.class){
                try {
                    HorseRace.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("STARTED: " + Thread.currentThread());
            // todo: run through a long loop or do some other exertion
            loop(1000); // loop already warmed up at this point of time
            System.out.println("FINISHED: " + Thread.currentThread());
        };
        // todo: create a new thread for each priority (in a for loop) and start
        // it
        for (int priority = Thread.MIN_PRIORITY; priority <= Thread.MAX_PRIORITY ; priority++) {
            Thread thread = new Thread(race);
            thread.setPriority(priority);
            thread.start();
        }
        System.out.println("on your marks ...");
        Thread.sleep(1000);
        System.out.println("get set ...");
        Thread.sleep(1000);
        System.out.println("GO!!!");
        // todo: notify all threads waiting on the common lock object
        synchronized (HorseRace.class){
            HorseRace.class.notifyAll();
        }
    }

    private static void test() {
        long time = System.nanoTime();
        try{

            loop(1000);
        }finally {
            time = System.nanoTime() - time;
            System.out.printf("time = %dms%n",(time/1_000_000));
        }
    }

    public static double loop(int upto) {
        long total = 0;
        for (long i = 0; i < upto * upto * upto; i++) {

            total += i;

        }
        return total;
    }
}
