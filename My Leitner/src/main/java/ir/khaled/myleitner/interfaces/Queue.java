package ir.khaled.myleitner.interfaces;

/**
 * Created by kh.bakhtiari on 5/26/2014.
 */
public interface Queue<T> {

    public boolean enqueue(T object);

    void dequeue();

    public static interface OnItemDequeuedListener<T> {
        public void onItemDequeued(T queueItem);
    }
}
