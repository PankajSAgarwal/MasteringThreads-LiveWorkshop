package playground;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class SetContentionDemo {
    private static volatile boolean running = true;
    private static final LongAdder addCounter = new LongAdder();
    private static final LongAdder removeCounter = new LongAdder();
    private static final LongAdder containsCounter = new LongAdder();

    public static void main(String... args) throws InterruptedException {
        runTest(new ConcurrentSkipListSet<>());
        runTest(ConcurrentHashMap.newKeySet());
        runTest(Collections.newSetFromMap(new Hashtable<>()));
    }

    private static void runTest(Set<Integer> set) throws InterruptedException {
        System.out.println("Testing " + set.getClass());
        addCounter.reset();
        removeCounter.reset();
        containsCounter.reset();
        running = true;
        test(set);
        Thread.sleep(10_000);
        running = false;
        Thread.sleep(50);
        System.out.printf(Locale.US, "addCounter = %,d%n", addCounter.longValue());
        System.out.printf(Locale.US, "removeCounter = %,d%n", removeCounter.longValue());
        System.out.printf(Locale.US, "containsCounter = %,d%n", containsCounter.longValue());
    }

    private static void test(Set<Integer> set) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            list.add(i);
        }
        Collections.shuffle(list, new Random(0));
        set.addAll(list);

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                long counter = 0;
                while (running) {
                    set.add(42);
                    counter++;
                }
                addCounter.add(counter);
            }).start();
            new Thread(() -> {
                long counter = 0;
                while (running) {
                    set.remove(42);
                    counter++;
                }
                removeCounter.add(counter);
            }).start();
            new Thread(() -> {
                long counter = 0;
                while (running) {
                    set.contains(42);
                    counter++;
                }
                containsCounter.add(counter);
            }).start();
        }

    }
}
