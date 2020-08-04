package masteringthreads.ch5_threading_problems.solution_5_2;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ProcessTaskManager implements Runnable {
    final ProcessTaskProxy ptp = new ProcessTaskProxy();

    public void run() {
        HashMap map = new HashMap();
        map.put(Thread.currentThread().getName(), "V-" + Thread.currentThread().getName());
        ThreadLocalContextHolder.put(map);
        ptp.printRCEKey();
    }
}
