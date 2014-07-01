package ir.khaled.myleitner.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import ir.khaled.myleitner.library.Util;

/**
 * Created by kh.bakhtiari on 5/15/2014.
 */
public class AppButton extends Button {

    public AppButton(Context context) {
        super(context);
        constructor();
    }

    public AppButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        constructor();
    }

    public AppButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        constructor();
    }

    private void constructor() {
        if (this.isInEditMode()) return;
        Typeface typeface = Util.getAppFontUI(getContext());
        this.setTypeface(typeface);
    }
}