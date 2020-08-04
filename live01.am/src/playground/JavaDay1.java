package playground;

public class JavaDay1 {
    public static void main(String... args) throws InterruptedException {
        Object obj = new Object();
        obj.getClass();
        obj.toString();
        obj.equals(obj);
        obj.hashCode();
        obj.notify();
        obj.notifyAll();
        obj.wait();
        obj.wait(10);
        obj.wait(10, 10);

    }
}
