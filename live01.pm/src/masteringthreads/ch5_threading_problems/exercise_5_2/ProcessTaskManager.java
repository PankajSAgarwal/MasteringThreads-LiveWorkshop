package masteringthreads.ch5_threading_problems.exercise_5_2;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ProcessTaskManager implements Runnable {
    final ProcessTaskProxy ptp = new ProcessTaskProxy();
    private final AtomicReference<Map> mapRef = new AtomicReference<>();

//    public void run() {
//        ThreadLocalContextHolder.put( Thread.currentThread().getName());
//        ptp.printRCEKey();
//    }

    public void run() {
        Map map = mapRef.get();
        if (map == null) {
            LockSupport.parkNanos(10_000_000);
            map = new ConcurrentHashMap();
            Map result = mapRef.compareAndExchange(null, map);
            if (result != null) map = result;
        }
        map.put(Thread.currentThread().getName(), "V-" + Thread.currentThread().getName());
        ThreadLocalContextHolder.put(map);
        ptp.printRCEKey();
    }
}
