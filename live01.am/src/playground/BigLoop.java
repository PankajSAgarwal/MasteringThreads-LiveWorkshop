package playground;

public class BigLoop {

    public static final int UPTO = 10000;

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
            System.out.printf("time = %dms%n", (time / 1000000));
        }
    }

    private static void f0() {
        for (int i0 = 0; i0 < UPTO; i0++) {
            for (int i1 = 0; i1 < UPTO; i1++) {
                for (int i2 = 0; i2 < UPTO; i2++) {
                    for (int i3 = 0; i3 < UPTO; i3++) {
//                        for (int i4 = 0; i4 < 1000; i4++) {
//                             do nothing
//                        }
                    }
                }
            }
        }
    }

}
