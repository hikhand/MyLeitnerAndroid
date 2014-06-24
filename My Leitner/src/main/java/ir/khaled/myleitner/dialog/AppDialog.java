package ir.khaled.myleitner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.helper.Logger;
import ir.khaled.myleitner.view.AppTextView;

/**
 * Created by kh.bakhtiari on 5/15/2014.
 */
public class AppDialog extends Dialog {
    public static final int ID_BTN_POS = R.id.btn_right;
    public static final int ID_BTN_NEG = R.id.btn_left;
    protected Context context;
    private String LOG_NAME = "appDialog";
    private AppDialogTransparent dialogBack;
    private View v_root;
    private ViewGroup vg_rootTitle;
    private ViewGroup vg_rootContent;
    private AppTextView btn_positive;
    private AppTextView btn_negative;
    private AppTextView btn_neutral;
    private AppTextView tv_loadingMessage;
    private View v_lineTitle;
    private View v_lineButtons;
    private View v_lineButtons_left;
    private View v_lineButtons_right;
    private View v_loading;
    private ProgressBar pb_loading;
    private boolean buttonsSet;

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

        LayoutInflater inflater = LayoutInflater.from(context);
        v_root = inflater.inflate(R.layout.dialog, null);
        super.setContentView(v_root);
    }

    /**
     * adds this dialog to dialog queue.
     */
    @Override
    public void show() {
        DialogPublisher.getInstance().addToQueue(this);
    }

    /**
     * shows dialog this method should only be called from {@link ir.khaled.myleitner.dialog.DialogPublisher}.
     */
    public void showDialog() {
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
        AppTextView tv_title = (AppTextView) LayoutInflater.from(context).inflate(R.layout.textview_overall, null);
        tv_title.setText(title);
        setTitle(tv_title);
    }

    @Override
    public void setTitle(int titleId) {
        AppTextView tv_title = (AppTextView) LayoutInflater.from(context).inflate(R.layout.textview_overall, null);
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.font_large));
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
        vg_rootTitle.setVisibility(View.VISIBLE);
        showLineTitle();
        vg_rootTitle.addView(viewTitle);
    }

    public void setContentView(String message) {
        AppTextView tv_message = (AppTextView) LayoutInflater.from(context).inflate(R.layout.textview_overall, vg_rootContent, false);
        tv_message.setMultiLine(true);
        tv_message.setText(message);
        setContentView(tv_message);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(LayoutInflater.from(context).inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View viewContent) {
        if (vg_rootContent == null)
            vg_rootContent = (ViewGroup) v_root.findViewById(R.id.v_content);

        vg_rootContent.removeAllViews();
        if (vg_rootContent.getChildCount() > 0) {
            Logger.LogD(LOG_NAME, "trying to set dialog's Content but it already has a Content");
            return;
        }
        vg_rootContent.setVisibility(View.VISIBLE);
        vg_rootContent.addView(viewContent);
    }

    private void showLineTitle() {
        if (v_lineTitle == null)
            v_lineTitle = v_root.findViewById(R.id.v_lineTitle);

        v_lineTitle.setVisibility(View.VISIBLE);
    }

    public void setPositiveButton(String text, View.OnClickListener onClick) {
        buttonsSet = true;
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

    private void hidePositiveButton() {
        if (btn_positive == null)
            btn_positive = (AppTextView) v_root.findViewById(R.id.btn_right);

        btn_positive.setVisibility(View.GONE);
    }

    public void setNegativeButton(String text, View.OnClickListener onClick) {
        buttonsSet = true;
        showLineButtons();

        if (btn_negative == null)
            btn_negative = (AppTextView) v_root.findViewById(R.id.btn_left);

        btn_negative.setVisibility(View.VISIBLE);
        btn_negative.setText(text);

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

    private void hideNegativeButton() {
        if (btn_negative == null)
            btn_negative = (AppTextView) v_root.findViewById(R.id.btn_left);

        btn_negative.setVisibility(View.GONE);
    }

    public void setNeutralButton(String text, View.OnClickListener onClick) throws IllegalAccessException {
        buttonsSet = true;
        showLinesButtons();
        throw new IllegalAccessException("No Neutral Button");
    }

    public void hideButtons() {
        v_root.findViewById(R.id.ll_buttons).setVisibility(View.GONE);
//        hideLineButtons();
//        hideLineButtonsLeft();
//        hideLineButtonsRight();
//        hideNegativeButton();
//        hidePositiveButton();
    }

    private void showLineButtons() {
        if (v_lineButtons == null)
            v_lineButtons = v_root.findViewById(R.id.v_lineButtons);

        v_lineButtons.setVisibility(View.VISIBLE);
    }

    private void hideLineButtons() {
        if (v_lineButtons == null)
            v_lineButtons = v_root.findViewById(R.id.v_lineButtons);

        v_lineButtons.setVisibility(View.GONE);
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

    private void hideLineButtonsLeft() {
        if (v_lineButtons_left == null)
            v_lineButtons_left = v_root.findViewById(R.id.v_lineLeft);

        v_lineButtons_left.setVisibility(View.GONE);
    }

    private void showLineButtonsRight() {
        if (v_lineButtons_right == null)
            v_lineButtons_right = v_root.findViewById(R.id.v_lineRight);

        v_lineButtons_right.setVisibility(View.VISIBLE);
    }

    private void hideLineButtonsRight() {
        if (v_lineButtons_right == null)
            v_lineButtons_right = v_root.findViewById(R.id.v_lineRight);

        v_lineButtons_right.setVisibility(View.GONE);
    }

    public void startLoading() {
        startLoading(null);
    }

    public void startLoading(String message) {
        if (v_loading == null)
            v_loading = v_root.findViewById(R.id.v_loading);

        v_loading.setVisibility(View.VISIBLE);

        if (pb_loading == null)
            pb_loading = (ProgressBar) v_root.findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.VISIBLE);

        if (message != null) {
            if (tv_loadingMessage == null)
                tv_loadingMessage = (AppTextView) v_root.findViewById(R.id.tv_loadingMessage);
            tv_loadingMessage.setText(message);
            tv_loadingMessage.setVisibility(View.VISIBLE);
        }

        hideContentView();
        hideButtons();
    }

    public void stopLoading() {
        if (v_loading == null)
            v_loading = v_root.findViewById(R.id.v_loading);

        v_loading.setVisibility(View.GONE);

        showContentView();
        if (buttonsSet)
            v_root.findViewById(R.id.ll_buttons).setVisibility(View.VISIBLE);

    }

    public void showError() {
        showError(context.getString(R.string.errorConnection));
    }

    public void showError(String message) {
        showError(message, null, null);
    }

    public void showError(String message, String textButton, View.OnClickListener onClickListener) {
        showError(message, textButton, null, onClickListener, null);
    }

    public void showError(String message, String textButtonPos, String textButtonNeg, View.OnClickListener onClickPos, View.OnClickListener onClickNeg) {
        if (v_loading == null)
            v_loading = v_root.findViewById(R.id.v_loading);

        v_loading.setVisibility(View.VISIBLE);

        if (pb_loading == null)
            pb_loading = (ProgressBar) v_root.findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.GONE);

        if (tv_loadingMessage == null)
            tv_loadingMessage = (AppTextView) v_root.findViewById(R.id.tv_loadingMessage);
        tv_loadingMessage.setText(message);
        tv_loadingMessage.setVisibility(View.VISIBLE);

        textButtonPos = textButtonPos == null ? context.getString(R.string.ok) : textButtonPos;
        setPositiveButton(textButtonPos, onClickPos);

        if (onClickNeg != null) {
            textButtonNeg = textButtonNeg == null ? context.getString(R.string.cancel) : textButtonNeg;
            setNegativeButton(textButtonNeg, onClickNeg);
        }
        hideContentView();
        v_root.findViewById(R.id.ll_buttons).setVisibility(View.VISIBLE);
    }

    private void hideContentView() {
        if (vg_rootContent == null)
            vg_rootContent = (ViewGroup) v_root.findViewById(R.id.v_content);
        vg_rootContent.setVisibility(View.GONE);
    }

    private void showContentView() {
        if (vg_rootContent == null)
            vg_rootContent = (ViewGroup) v_root.findViewById(R.id.v_content);
        vg_rootContent.setVisibility(View.VISIBLE);
    }

    public String getDialogName() {
        return LOG_NAME;
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
            view.setBackgroundColor(Color.parseColor("#99000000"));

            this.setContentView(view);
        }
    }
}
