package playground;

public class EmptyLoops {

    public static final int UPTO = 1_000_000;

    public static void main(String... args) {
        for (int i = 0; i < 30; i++) {
            test();
        }
    }

    private static void test() {
        long time = System.nanoTime();
        try {
            f0();
        } finally {
            time = System.nanoTime() - time;
            System.out.printf("time = %dms%n", (time / 1_000_000));
        }
    }

    private static void f0() {
        for (int i = 0; i < UPTO; i++) {
            f1();
        }
    }
    private static void f1() {
        for (int i = 0; i < UPTO; i++) {
            f2();
        }
    }
    private static void f2() {
        for (int i = 0; i < UPTO; i++) {
            f3();
        }
    }
    private static void f3() {
        for (int i = 0; i < UPTO; i++) {
            f4();
        }
    }
    private static void f4() {
        for (int i = 0; i < UPTO; i++) {
            f5();
        }
    }

    private static void f5() {
        // do nothing
    }
}
