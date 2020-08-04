package playground;

import java.text.*;
import java.util.*;

public class DateFormatDemo {
    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private synchronized static Date parse(String date) {
        try {
            return format.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date", e);
        }
    }

    public static void main(String... args) {
        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(() -> {
                System.out.println("Starting " + Thread.currentThread());
                long time = System.nanoTime();
                try {
                    for (int j = 0; j < 1_000_000; j++) {
                        parse("2020-04-31");
                    }
                } finally {
                    time = System.nanoTime() - time;
                    System.out.printf("time = %dms%n", (time / 1_000_000));
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }
}
