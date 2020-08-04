package playground;

public class MemoryPuzzle {

    public static final int SIZE = (int) ((Runtime.getRuntime().maxMemory() * 0.7 / 8));

    public static void main(String... args) throws InterruptedException {
        for (int i = 0; i < 9999; i++) {
            test(64);
        }
        Thread.sleep(500);
        test(SIZE);
    }

    private static void test(int size) {
        {
            long[] data1 = new long[size];
        }

        long[] data1 = new long[size];
    }
}
