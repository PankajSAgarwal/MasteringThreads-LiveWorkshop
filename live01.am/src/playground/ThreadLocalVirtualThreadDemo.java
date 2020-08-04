package playground;

public class ThreadLocalVirtualThreadDemo {
    private static final ThreadLocal<Long> createTimeNanos =
        ThreadLocal.withInitial(() -> System.nanoTime());

    public static void main(String... args) throws InterruptedException {
        Runnable task = () -> {
            createTimeNanos.get();
            beFriendly();
        };
//        Thread t = new Thread(task);
//        t.start();
        System.out.println();

        Thread t = Thread.startVirtualThread(task);
        t.join();
        Thread t3 = Thread.builder().disallowThreadLocals().virtual().task(task).start();
        t3.join();
    }

    private static void beFriendly() {
        for (int i = 0; i < 100; i++) {
            System.out.println("Hello world");
        }
        howLongDidThatTake();
    }

    private static void howLongDidThatTake() {
        System.out.println((System.nanoTime() - createTimeNanos.get()) / 1000 + " µs");
    }
}
