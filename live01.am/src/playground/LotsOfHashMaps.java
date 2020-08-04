package playground;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class LotsOfHashMaps {
    public static void main(String... args) {
        new Thread(new Runnable() {
            public void run() {
                allocateHashMap(80000);
            }
        }, "HashMapThread").start();
        new Thread(new Runnable() {
            public void run() {
                allocateConcurrentHashMap(80000);
            }
        }, "ConcurrentHashMapThread").start();
    }

    private static void allocateHashMap(int number) {
        for (int i = 0; i < number; i++) {
            Map<Integer, Integer> map = new HashMap<>();
            map.put(i, i * 2);
            map.put(i + 1, i * i);
        }
        System.out.println("Finished creating HashMaps");
        LockSupport.park();
    }

    private static void allocateConcurrentHashMap(int number) {
        for (int i = 0; i < number; i++) {
            Map<Integer, Integer> map = new ConcurrentHashMap<>();
            map.put(i, i * 2);
            map.put(i + 1, i * i);
        }
        System.out.println("Finished creating ConcurrentHashMaps");
        LockSupport.park();
    }
}
