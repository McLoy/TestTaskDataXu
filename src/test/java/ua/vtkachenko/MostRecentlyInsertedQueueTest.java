package ua.vtkachenko;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.fest.assertions.Assertions.assertThat;

public class MostRecentlyInsertedQueueTest {
    private MostRecentlyInsertedQueue<Integer> queue;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        queue = new MostRecentlyInsertedQueue<>(3);
    }

    @Test
    public void shouldBeEmptyOnCreation() throws Exception {
        assertThat(queue).isEmpty();
        assertThat(queue.peek()).isNull();
        assertThat(queue.poll()).isNull();
        expectedException.expect(NoSuchElementException.class);
        queue.element();
        expectedException.expect(NoSuchElementException.class);
        queue.remove();
    }

    @Test
    public void shouldOfferElementsToTheEnd() throws Exception {
        queue.offer(1);
        queue.offer(2);
        assertThat(queue).hasSize(2);
        assertThat(queue.peek()).isEqualTo(1);
    }

    @Test
    public void shouldPeekFirstElement() throws Exception {
        queue.offer(2);
        queue.offer(3);
        assertThat(queue.peek()).isEqualTo(2);
        assertThat(queue).hasSize(2);
    }

    @Test
    public void shouldPollFirstElement() throws Exception {
        queue.offer(1);
        queue.offer(2);
        assertThat(queue).hasSize(2);
        assertThat(queue.poll()).isEqualTo(1);
        assertThat(queue).hasSize(1);
        assertThat(queue.poll()).isEqualTo(2);
        assertThat(queue).isEmpty();
    }

    @Test
    public void shouldKickOutFirstElementWhenOverFlow() throws Exception {
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        assertThat(queue).hasSize(3);
        assertThat(queue.poll()).isEqualTo(2);
    }

    @Test
    public void clear() throws Exception {
        queue.offer(1);
        queue.offer(2);
        queue.clear();
        assertThat(queue).isEmpty();
    }

    @Test
    public void toStringTest() throws Exception {
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        assertThat(queue.toString()).isEqualTo("[1, 2, 3]");
    }
}