package ir.khaled.myleitner.library;

import java.util.ArrayList;

import ir.khaled.myleitner.dialog.AppDialog;
import ir.khaled.myleitner.interfaces.Queue;

/**
 * Created by kh.bakhtiari on 5/26/2014.
 */
public class ArrayQueue<T> extends ArrayList<T> implements Queue<T> {

    OnItemDequeuedListener<T> itemDequeuedListener;

    public ArrayQueue(OnItemDequeuedListener<T> itemDequeuedListener) {
        this.itemDequeuedListener = itemDequeuedListener;
    }

    /**
     * is queue ready to dequeue ?
     */
    private boolean isOneInFront;

    /**
     * the same as {@link #add(Object)}
     * adds an item to the queue
     */
    @Override
    public boolean enqueue(T object) {
        return add(object);
    }

    /**
     * removes the first existing added item from queue and publish it by
     * {@link ir.khaled.myleitner.interfaces.Queue.OnItemDequeuedListener#onItemDequeued(Object)} <br/>
     * <p/>
     * after item is dequeue completely {@link #dequeueFinished()} should be called.
     */
    @Override
    public void dequeue() {
        if (isOneInFront)
            return;

        T first = getFirstItem();
        if (first == null)
            return;

        isOneInFront = true;
        itemDequeuedListener.onItemDequeued(first);
    }

    @Override
    public boolean add(T object) {
        if (contains(object))
            return false;

        super.add(object);
        dequeue();
        return true;
    }

    private T getFirstItem() {
        if (size() == 0)
            return null;
        return get(0);
    }


    /**
     * after an item is completely dequeued this method should be called
     * to dequeue the next item
     */
    public void dequeueFinished(AppDialog dialog) {
        if (!isOneInFront)
            return;

        remove(dialog);
        isOneInFront = false;
        dequeue();
    }

    public void setNothingIsInFront() {
        this.isOneInFront = false;
    }
}