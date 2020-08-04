package playground;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class DistinctFIFOQueue<E> {
    private final Set<Node<E>> nodes = new ConcurrentSkipListSet<>();
    private final AtomicInteger nextNodeId = new AtomicInteger();
    private class Node<E> implements Comparable<Node<E>>{
        private final int id = nextNodeId.incrementAndGet();
        private final E e;

        public Node(E e) {
            this.e = e;
        }

        @Override
        public int compareTo(Node<E> that) {
            if (this.e.equals(that.e)) return 0;
            return Integer.compare(this.id, that.id);
        }

        @Override
        public String toString() {
            return e.toString();
        }
    }
    public boolean offer(E e) {
        return nodes.add(new Node<>(e));
    }

    public E poll() {
        Iterator<Node<E>> iterator = nodes.iterator();
        if (iterator.hasNext()) return iterator.next().e;
        return null;
    }

    @Override
    public String toString() {
        return nodes.toString();
    }
}
