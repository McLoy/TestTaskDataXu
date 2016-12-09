package ua.vtkachenko;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class MostRecentlyInsertedBlockingQueue<E> extends MostRecentlyInsertedQueue<E> implements BlockingQueue<E> {
    public MostRecentlyInsertedBlockingQueue(int capacity) {
        super(capacity);
    }

    @Override
    public synchronized void put(E e) throws InterruptedException {
        while (size >= capacity) {
            wait();
        }
        offer(e);
        notifyAll();
    }

    @Override
    public synchronized boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        long endTime = System.currentTimeMillis() + unit.toMillis(timeout);
        while (size >= capacity && System.currentTimeMillis() < endTime) {
            long timLeft = System.currentTimeMillis() - unit.toMillis(timeout);
            if (timLeft > 0) {
                wait(timLeft);
            }
        }

        if (size < capacity) {
            offer(e);
            return true;
        }

        return false;
    }

    @Override
    public synchronized E take() throws InterruptedException {
        while (size == 0) {
            wait();
        }
        E value = poll();
        notifyAll();
        return value;
    }

    @Override
    public synchronized E poll(long timeout, TimeUnit unit) throws InterruptedException {
        long endTime = System.currentTimeMillis() + unit.toMillis(timeout);
        while (size == 0 && System.currentTimeMillis() < endTime) {
            long timLeft = System.currentTimeMillis() - unit.toMillis(timeout);
            if (timLeft > 0) {
                wait(timLeft);
            }
        }

        if (size > 0) {
            E value = poll();
            notifyAll();
            return value;
        }

        return null;
    }

    @Override
    public synchronized int remainingCapacity() {
        return capacity - size;
    }

    @Override
    public synchronized int drainTo(Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
    }

    @Override
    public synchronized int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }
        if (maxElements <= 0) {
            return 0;
        }
        int n = Math.min(maxElements, size);
        for (int i = 0; i < n; i++) {
            c.add(poll());
        }
        return n;
    }

    @Override
    public synchronized void clear() {
        super.clear();
    }

    @Override
    public synchronized int size() {
        return super.size();
    }

    @Override
    public synchronized boolean offer(E e) {
        return super.offer(e);
    }

    @Override
    public synchronized E poll() {
        return super.poll();
    }

    @Override
    public synchronized E peek() {
        return super.peek();
    }

    @Override
    public synchronized String toString() {
        return super.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new MostRecentlyInsertedQueueIterator() {
            @Override
            public boolean hasNext() {
                synchronized (MostRecentlyInsertedBlockingQueue.this) {
                    return super.hasNext();
                }
            }

            @Override
            public E next() {
                synchronized (MostRecentlyInsertedBlockingQueue.this) {
                    if (cursor == null) {
                        throw new NoSuchElementException();
                    }

                    E value = cursor.value;
                    cursor = cursor.next;

                    return value;
                }
            }
        };
    }
}
