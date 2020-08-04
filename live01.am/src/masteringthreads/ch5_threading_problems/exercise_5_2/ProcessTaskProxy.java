package masteringthreads.ch5_threading_problems.exercise_5_2;

import java.util.*;

@SuppressWarnings("rawtypes")
public class ProcessTaskProxy {
    public void printRCEKey() {
        Map map = (Map) ThreadLocalContextHolder.get();
        Object val = map.get(Thread.currentThread().getName());
        if (val == null) val = map;
        System.out.println("Thread Name : " + Thread.currentThread().getName() +
            "\t TL Value : " + val);
        ThreadLocalContextHolder.cleanupThread();
    }
}
