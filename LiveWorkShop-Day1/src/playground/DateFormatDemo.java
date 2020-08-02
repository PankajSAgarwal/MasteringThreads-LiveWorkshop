package playground;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatDemo {



    private static Date parse(String date) {
        try {
             DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date", e);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(() -> {
                long time = System.nanoTime();
                try {
                    for (int j = 0; j < 1_000_000; j++) {
                        Date date = parse("2020-04-30");
                        //System.out.println("date.toString() = " + date.toString());
                    }
                } finally {
                    time = System.nanoTime() - time;
                    System.out.printf("time = %dms%n", (time / 1_000_000));
                }
            });
            thread.start();
        }
    }
}
