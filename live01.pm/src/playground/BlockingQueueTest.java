package playground;

import java.util.concurrent.*;

public class BlockingQueueTest {

    public static void main(String... args) {
        {
            BlockingQueue<Integer> q = new LinkedTransferQueue<>();
            System.out.println(q.offer(2));
            System.out.println(q.offer(42));
            System.out.println(q.offer(88));
            System.out.println(q.offer(7));
            System.out.println(q);

            System.out.println(q.offer(42));
            System.out.println(q);
            System.out.println(q.offer(142));
            System.out.println(q.offer(142));
            System.out.println(q);
        }

        System.out.println();

        {
            DistinctFIFOQueue<Integer> q = new DistinctFIFOQueue<>();
            System.out.println(q.offer(2));
            System.out.println(q.offer(42));
            System.out.println(q.offer(88));
            System.out.println(q.offer(7));
            System.out.println(q);

            System.out.println(q.offer(42));
            System.out.println(q);
            System.out.println(q.offer(142));
            System.out.println(q.offer(142));
            System.out.println(q);
        }
    }
}
