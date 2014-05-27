package ir.khaled.myleitner.dialog;

import android.content.Context;
import android.view.View;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.helper.Util;

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
    public static void showIfFirstRun(Context context) {
        if (!Util.isAppFirstRun(context))
            return;

        new Welcome(context).show();
    }

    @Override
    public void show() {
        setTitle(R.string.welcomeTitle);
        setContentView(context.getResources().getString(R.string.welcomeText));
        setPositiveButton(context.getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        super.show();
    }
}
