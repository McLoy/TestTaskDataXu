package ua.vtkachenko;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;

public class MostRecentlyInsertedQueue<E> extends AbstractQueue<E> implements Queue<E> {

    private Entry<E> header;
    private int capacity;
    private int size = 0;

    public MostRecentlyInsertedQueue(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException();
        }

        header = new Entry(null, header, header);
        header.next = header.prev = header;
        this.capacity = capacity;
        this.size = 0;
    }

    @Override
    public void clear() {
        Iterator it = iterator();
        while (it.hasNext()) {
            poll();
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Iterator it = iterator();
        while (it.hasNext()) {
            if (str.length() == 0) {
                str = str.append(it.next());
            } else {
                str = str.append(str).append(it.next()).append(it.next());
            }
        }
        return "[ " + str + " ]";
    }

    private static class Entry<T> {
        T element;
        Entry<T> next;
        Entry<T> prev;

        Entry(T element, Entry<T> next, Entry<T> prev) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private class MostRecentlyInsertedQueueIterator<T> implements Iterator<T> {

        private Entry cursor = header;

        @Override
        public boolean hasNext() {

            return cursor.next != header && cursor.next != cursor;
        }

        @Override
        public T next() {
            if (cursor.next != header && cursor.next != cursor) {
                cursor = cursor.next;
                return (T) cursor.element;
            }
            return null;
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
        if (size >= capacity) {
            poll();
        }
        addEntry(e);
        return size > 0;
    }

    private void addEntry(E e) {
        Entry newEntry = new Entry(e, header, header.prev);
        newEntry.prev.next = newEntry;
        newEntry.next.prev = newEntry;
        size++;
    }

    @Override
    public E poll() {
        Entry entry = header.next;
        E value = (E) entry.element;
        clearInnerLinks(entry);
        return value;
    }

    private void clearInnerLinks(Entry<E> x) {
        x.prev.next = x.next;
        x.next.prev = x.prev;
        x.next = null;
        x.prev = null;
        x.element = null;
        size--;
    }

    @Override
    public E peek() {
        return (E) header.next.element;
    }
}
