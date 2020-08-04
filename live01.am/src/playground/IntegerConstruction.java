package playground;

public class IntegerConstruction {
    public static void main(String... args) {
        String lock = "ReadLock";
        Integer integer1 = Integer.valueOf(142);
        Integer integer2 = Integer.valueOf(142);
        System.out.println(integer1 == integer2);
    }
}
