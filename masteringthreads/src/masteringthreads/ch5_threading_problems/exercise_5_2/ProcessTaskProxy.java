package masteringthreads.ch5_threading_problems.exercise_5_2;

import java.util.*;

@SuppressWarnings("rawtypes")
public class ProcessTaskProxy {
    HashMap map = null;

//    public void printRCEKey(){
//        
//        System.out.println ( Thread.currentThread().getName() + " \t " + ThreadLocalContextHolder.get() );
//        ThreadLocalContextHolder.cleanupThread();
//        //System.out.println ( Thread.currentThread().getName() + "\t" + ThreadLocalContextHolder.get()  + " after cleanup");
//    }

    public void printRCEKey() {
        HashMap map = (HashMap) ThreadLocalContextHolder.get();
        System.out.println("Thread Name : " + Thread.currentThread().getName() + "\t TL Value : " + map.get(Thread.currentThread().getName()));
        ThreadLocalContextHolder.cleanupThread();
    }
}
