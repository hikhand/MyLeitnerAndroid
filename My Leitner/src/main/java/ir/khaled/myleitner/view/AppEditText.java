package ir.khaled.myleitner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.helper.Util;

/**
 * Created by kh.bakhtiari on 1/21/14.
 */
public class AppEditText extends EditText {
    boolean multiLine;
    boolean textWatcherIsSet;

    public AppEditText(Context context, boolean multiLine) {
        super(context);
        this.multiLine = multiLine;
        init(null);
    }

    public AppEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(null);
    }

    public AppEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AppEditText);
            multiLine = a.getBoolean(R.styleable.AppEditText_multiLine, false);
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
        Typeface typeface = Util.getAppFontUI(getContext());
        this.setTypeface(typeface);
    }

    private void setTypeFaceMultiple() {
        if (this.isInEditMode()) return;
        Typeface typeface = Util.getAppFontMultiple(getContext());
        this.setTypeface(typeface);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        if (textWatcherIsSet)
            return;
        textWatcherIsSet = true;
        super.addTextChangedListener(watcher);
    }
}
