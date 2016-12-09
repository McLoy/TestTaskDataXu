package ua.vtkachenko;

import java.util.AbstractQueue;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MostRecentlyInsertedQueue<E> extends AbstractQueue<E> implements Queue<E> {

    protected Entry<E> head;
    protected Entry<E> tail;
    protected int capacity;
    protected int size = 0;
    protected long modifications;

    public MostRecentlyInsertedQueue(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
    }

    @Override
    public void clear() {
        for (Entry<E> entry = head; entry != null; ) {
            Entry next = entry.next;

            entry.prev = null;
            entry.value = null;
            entry.next = null;
            entry = next;
        }

        head = null;
        tail = null;
        size = 0;
        modifications++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (E e : this) {
            sb.append(e)
                    .append(", ");
        }
        if (size() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(']');
        return sb.toString();
    }

    protected static class Entry<T> {
        T value;
        Entry<T> next;
        Entry<T> prev;

        Entry(Entry<T> prev, T value, Entry<T> next) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    protected class MostRecentlyInsertedQueueIterator implements Iterator<E> {
        protected Entry<E> cursor = head;
        protected long modSnaphot = modifications;

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public E next() {
            if (cursor == null) {
                throw new NoSuchElementException();
            }

            if (modifications != modSnaphot) {
                throw new ConcurrentModificationException();
            }

            E value = cursor.value;
            cursor = cursor.next;

            return value;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new MostRecentlyInsertedQueueIterator();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }

        addToTail(e);

        if (size > capacity) {
            poll();
        }

        return true;
    }

    private void addToTail(E value) {
        Entry<E> newEntry = new Entry<E>(tail, value, null);
        Entry<E> oldTail = tail;
        tail = newEntry;
        if (oldTail == null) {

            head = newEntry;
        } else {
            oldTail.next = newEntry;
        }

        size++;
        modifications++;
    }

    @Override
    public E poll() {
        if (head == null) {
            return null;
        }

        Entry<E> oldHead = head;
        E value = head.value;
        head = head.next;
        oldHead.prev = null;
        oldHead.value = null;
        oldHead.next = null;

        if (head == null) {

            tail = null;
        } else {
            head.prev = null;
        }

        size--;
        modifications++;
        return value;
    }

    @Override
    public E peek() {
        return head == null ? null : head.value;
    }
}