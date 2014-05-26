package ir.khaled.myleitner.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.helper.Logger;

/**
 * Created by kh.bakhtiari on 5/15/2014.
 */
public class AppDialog extends Dialog {
    private String LOG_NAME = "appDialog";
    private Context context;
    private AppDialogTransparent dialogBack;
    private View v_root;
    private ViewGroup vg_rootTitle;
    private ViewGroup vg_rootContent;
    private AppTextView btn_positive;
    private AppTextView btn_negative;
    private AppTextView btn_neutral;
    private View v_lineContent;
    private View v_lineButtons;
    private View v_lineButtons_left;
    private View v_lineButtons_right;

    public AppDialog(Context context) {
        super(context, R.style.dialog_style);
        this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        v_root = inflater.inflate(R.layout.dialog, null);
        super.setContentView(v_root);
    }

    public AppDialog(Context context, String dialogName) {
        super(context, R.style.dialog_style);
        this.context = context;
        LOG_NAME = dialogName;
    }

    @Override
    public void show() {
        dialogBack = new AppDialogTransparent(context);
        dialogBack.show();
        super.show();
    }

    @Override
    public void dismiss() {
        dialogBack.dismiss();
        super.dismiss();
    }

    @Override
    public void setTitle(CharSequence title) {
        AppTextView tv_title = (AppTextView) LayoutInflater.from(context).inflate(R.layout.textview_overall, vg_rootTitle, false);
        tv_title.setText(title);
        setTitle(tv_title);
    }

    @Override
    public void setTitle(int titleId) {
        AppTextView tv_title = (AppTextView) LayoutInflater.from(context).inflate(R.layout.textview_overall, vg_rootTitle, false);
        tv_title.setTextSize(context.getResources().getDimension(R.dimen.font_large));
        tv_title.setText(context.getString(titleId));
        setTitle(tv_title);
    }

    public void setTitle(View viewTitle) {
        if (vg_rootTitle == null)
            vg_rootTitle = (ViewGroup) v_root.findViewById(R.id.v_title);

        if (vg_rootTitle.getChildCount() > 0) {
            Logger.LogD(LOG_NAME, "trying to set dialog's title but it already has a title");
            return;
        }

        vg_rootTitle.addView(viewTitle);
    }

    public void setContentView(String message) {
        AppTextView tv_message = (AppTextView) LayoutInflater.from(context).inflate(R.layout.textview_overall, vg_rootTitle, false);
        tv_message.setMultiLine(true);
        tv_message.setText(message);
        setContentView(tv_message);
    }

    @SuppressWarnings("ResourceType")
    public void setContentView(int messageId) {
        setContentView(context.getResources().getString(messageId));
    }

    public void setContentView(View viewContent) {
        if (vg_rootContent == null)
            vg_rootContent = (ViewGroup) v_root.findViewById(R.id.v_content);

        if (vg_rootContent.getChildCount() > 0) {
            Logger.LogD(LOG_NAME, "trying to set dialog's Content but it already has a Content");
            return;
        }
        vg_rootContent.addView(viewContent);
        showLineContent();
    }

    private void showLineContent() {
        if (v_lineContent == null)
            v_lineContent = v_root.findViewById(R.id.v_lineContent);

        v_lineContent.setVisibility(View.VISIBLE);
    }

    public void setPositiveButton(String text, View.OnClickListener onClick) {
        showLineButtons();

        if (btn_positive == null)
            btn_positive = (AppTextView) v_root.findViewById(R.id.btn_right);

        btn_positive.setVisibility(View.VISIBLE);
        btn_positive.setText(text);
        if (onClick == null) {
            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        } else {
            btn_positive.setOnClickListener(onClick);
        }
        showLinesButtons();
    }

    public void setNegativeButton(String text, View.OnClickListener onClick) {
        showLineButtons();

        if (btn_negative == null)
            btn_negative = (AppTextView) v_root.findViewById(R.id.btn_left);

        btn_negative.setVisibility(View.VISIBLE);

        if (onClick == null) {
            btn_negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            btn_negative.setOnClickListener(onClick);
        }
        showLinesButtons();
    }

    public void setNeutralButton(String text, View.OnClickListener onClick) throws IllegalAccessException {
        showLinesButtons();
        throw new IllegalAccessException("No Neutral Button");
    }

    private void showLineButtons() {
        if (v_lineButtons == null)
            v_lineButtons = v_root.findViewById(R.id.v_lineButtons);

        v_lineButtons.setVisibility(View.VISIBLE);
    }

    private void showLinesButtons() {
        int countVisible = 0;

        if (isBtnPositiveVisible())
            countVisible++;
        if (isBtnNegativeVisible())
            countVisible++;
        if (isBtnNeutralVisible())
            countVisible++;

        if (countVisible == 2) {
            showLineButtonsRight();
        } else if (countVisible == 3) {
            showLineButtonsRight();
            showLineButtonsLeft();
        }
    }

    private boolean isBtnPositiveVisible() {
        return btn_positive != null && btn_positive.getVisibility() == View.VISIBLE;
    }

    private boolean isBtnNegativeVisible() {
        return btn_negative != null && btn_negative.getVisibility() == View.VISIBLE;
    }

    private boolean isBtnNeutralVisible() {
        return btn_neutral != null && btn_neutral.getVisibility() == View.VISIBLE;
    }

    private void showLineButtonsLeft() {
        if (v_lineButtons_left == null)
            v_lineButtons_left = v_root.findViewById(R.id.v_lineLeft);

        v_lineButtons_left.setVisibility(View.VISIBLE);
    }

    private void showLineButtonsRight() {
        if (v_lineButtons_right == null)
            v_lineButtons_right = v_root.findViewById(R.id.v_lineRight);

        v_lineButtons_right.setVisibility(View.VISIBLE);
    }

    public static class Builder {
        Context context;

        private String title;
        private boolean contentIsString;
        private String contentMessage;
        private View contentView;
        private boolean positiveButtonIsSet;
        private String positiveButtonText;
        private View.OnClickListener onClickPositiveButton;
        private boolean negativeButtonIsSet;
        private String negativeButtonText;
        private View.OnClickListener onClickNegativeButton;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(int titleId) {
            this.title = context.getString(titleId);
            return this;
        }

        public Builder setContentView(String message) {
            this.contentMessage = message;
            contentIsString = true;
            return this;
        }

        public Builder setContentView(int messageId) {
            this.contentMessage = context.getString(messageId);
            contentIsString = true;
            return this;
        }

        public Builder setContentView(View view) {
            this.contentView = view;
            contentIsString = false;
            return this;
        }

        public Builder setPositiveButton(String text, View.OnClickListener onClickListener) {
            this.positiveButtonText = text;
            this.onClickPositiveButton = onClickNegativeButton;
            this.positiveButtonIsSet = true;
            return this;
        }

        public Builder setNegativeButton(String text, View.OnClickListener onClickListener) {
            this.negativeButtonText = text;
            this.onClickNegativeButton = onClickListener;
            this.negativeButtonIsSet = true;
            return this;
        }

        public AppDialog build() {
            AppDialog dialog = new AppDialog(context);

            if (title != null)
                dialog.setTitle(title);

            if (contentMessage != null || contentView != null) {
                if (contentIsString)
                    dialog.setContentView(contentMessage);
                else
                    dialog.setContentView(contentView);
            }

            if (positiveButtonIsSet)
                dialog.setPositiveButton(positiveButtonText, onClickPositiveButton);

            if (negativeButtonIsSet)
                dialog.setNegativeButton(negativeButtonText, onClickNegativeButton);

            return dialog;
        }
    }

    class AppDialogTransparent extends Dialog {

        public AppDialogTransparent(Context context) {
            super(context, R.style.dialog_transparent);

            LinearLayout view = new LinearLayout(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(Color.parseColor("#BF000000"));

            this.setContentView(view);
        }
    }
}
