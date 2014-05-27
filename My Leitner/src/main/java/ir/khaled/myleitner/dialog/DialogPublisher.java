package ir.khaled.myleitner.dialog;

import android.content.DialogInterface;

import ir.khaled.myleitner.interfaces.Queue;
import ir.khaled.myleitner.library.ArrayQueue;

/**
 * Created by kh.bakhtiari on 5/26/2014.
 */
public class DialogPublisher implements AppDialog.OnDismissListener, Queue.OnItemDequeuedListener<AppDialog>{
    private static DialogPublisher ourInstance;

    private ArrayQueue<AppDialog> queue;

    public static DialogPublisher getInstance() {
        if (ourInstance == null)
            ourInstance = new DialogPublisher();

        return ourInstance;
    }

    private DialogPublisher() {
        this.queue = new ArrayQueue<AppDialog>(this);
    }

    public void addToQueue(AppDialog dialog) {
        if (queue.enqueue(dialog))
            dialog.setOnDismissListener(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        queue.dequeueFinished((AppDialog) dialog);
    }

    private void showDialog(AppDialog dialog) {
        dialog.showDialog();
    }

    @Override
    public void onItemDequeued(AppDialog queueItem) {
        showDialog(queueItem);
    }

    public void nothingIsShown() {
        queue.setNothingIsInFront();
    }
}