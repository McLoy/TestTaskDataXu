package ua.vtkachenko;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Queue;

public class MostRecentlyInsertedQueueTest {

    private Queue<Integer> queue;

    @Before
    public void setUp() throws Exception {
        queue = new MostRecentlyInsertedQueue<>(3);
    }

    @Test
    public void size() throws Exception {
        Assertions.assertThat(queue.size()).isEqualTo(0);
    }

    @Test
    public void offer() throws Exception {
        queue.offer(1);
        Assertions.assertThat(queue.size()).isEqualTo(1);
    }

    @Test
    public void peek() throws Exception {
        queue.offer(2);
        queue.offer(3);
        Assertions.assertThat(queue.peek()).isEqualTo(2);
    }

    @Test
    public void poll() throws Exception {
        queue.offer(4);
        queue.offer(5);
        Assertions.assertThat(queue.poll()).isEqualTo(4);
    }

    @Test
    public void hugeOffer() throws Exception {
        queue.offer(6);
        queue.offer(7);
        queue.offer(8);
        queue.offer(9);
        Assertions.assertThat(queue.peek()).isEqualTo(7);
    }

    @Test
    public void clear() throws Exception {
        queue.offer(10);
        queue.offer(11);
        queue.clear();
        Assertions.assertThat(queue.size()).isEqualTo(0);
    }

    @Test
    public void writeString() throws Exception {
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        queue.offer(5);
        queue.poll();
        queue.poll();
        Assertions.assertThat(queue.toString()).isEqualTo("[5]");
    }
}
