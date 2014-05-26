package ir.khaled.myleitner.dialog;

import android.content.Context;

import ir.khaled.myleitner.view.AppDialog;

/**
 * Created by kh.bakhtiari on 5/25/2014.
 */
public class Welcome extends AppDialog {

    public Welcome(Context context) {
        super(context);
    }

    /**
     * shows welcome dialog only if its apps first run
     */
    public void showIfFirstRun() {

    }

    @Override
    public void show() {
        super.show();
    }
}
