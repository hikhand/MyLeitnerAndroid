package ir.khaled.myleitner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import ir.khaled.myleitner.Helper.Config;
import ir.khaled.myleitner.R;

/**
 * Created by kh.bakhtiari on 11/30/13.
 */
public class AppTextView extends android.widget.TextView {
    boolean multiLine;

    public AppTextView(Context context, boolean multiLine) {
        super(context);
        this.multiLine = multiLine;
        init(null);
    }

    public AppTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AppTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AppTextView);
            multiLine = a.getBoolean(R.styleable.AppTextView_multiLine, false);
            a.recycle();
        }
        if (multiLine) {
            setTypeFaceMultiple();
        } else {
            setTypeFaceUI();
        }
    }

    private void setTypeFaceUI() {
        if (this.isInEditMode()) return;
        Typeface typeface = Config.getAppFontUI(getContext());
        this.setTypeface(typeface);
    }

    private void setTypeFaceMultiple() {
        if (this.isInEditMode()) return;
        Typeface typeface = Config.getAppFontMultiple(getContext());
        this.setTypeface(typeface);
    }

}
