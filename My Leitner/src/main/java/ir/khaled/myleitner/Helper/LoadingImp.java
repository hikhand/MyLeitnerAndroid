package ir.khaled.myleitner.helper;

import android.content.Context;
import android.view.View;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.interfaces.Loading;
import ir.khaled.myleitner.view.AppButton;
import ir.khaled.myleitner.view.AppTextView;

/**
 * Created by kh.bakhtiari on 7/1/2014.
 */
public class LoadingImp implements Loading {

    Context context;
    View v_parent;
    View v_loadingRoot;
    View v_loading;
    View v_errorHorizontal;
    View v_errorVertical;

    AppTextView tv_errorHorizontal;
    AppTextView tv_errorVertical;
    AppButton btn_retryHorizontal;
    AppButton btn_retryVertical;

    public LoadingImp(Context context, View v_parent) {
        this.context = context;
        this.v_parent = v_parent;
        this.v_loadingRoot = v_parent.findViewById(R.id.v_loading);
    }

    @Override
    public void startLoading() {
        showLoading();
        hideErrorHorizontal();
        hideErrorVertical();
    }

    @Override
    public void stopLoading() {
        hideLoading();
        hideErrorHorizontal();
        hideErrorVertical();
    }

    @Override
    public void showError(String errorMessage, String textButton, View.OnClickListener onClickListener) {
        hideLoading();
        hideErrorHorizontal();
        showErrorVertical();

        getErrorMessageVertical().setText(errorMessage);
        getRetryVertical().setText(textButton);
        getRetryVertical().setOnClickListener(onClickListener);
    }

    public void showErrorHorizontal(String errorMessage, String textButton, View.OnClickListener onClickListener) {
        hideLoading();
        showErrorHorizontal();
        hideErrorVertical();

        getErrorMessageHorizontal().setText(errorMessage);
        getRetryHorizontal().setText(textButton);
        getRetryHorizontal().setOnClickListener(onClickListener);
    }


    private void showLoading() {
        getLoading().setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        getLoading().setVisibility(View.GONE);
    }

    private void showErrorHorizontal() {
        getErrorHorizontal().setVisibility(View.VISIBLE);
    }

    private void hideErrorHorizontal() {
        getErrorHorizontal().setVisibility(View.GONE);
    }

    private void showErrorVertical() {
        getErrorVertical().setVisibility(View.VISIBLE);
    }

    private void hideErrorVertical() {
        getErrorVertical().setVisibility(View.GONE);
    }

    private View getLoading() {
        if (v_loading == null)
            v_loading = v_parent.findViewById(R.id.pb_loading);
        return v_loading;
    }

    private View getErrorHorizontal() {
        if (v_errorHorizontal == null)
            v_errorHorizontal = v_parent.findViewById(R.id.v_errorHorizontal);
        return v_errorHorizontal;
    }

    private View getErrorVertical() {
        if (v_errorVertical == null)
            v_errorVertical = v_parent.findViewById(R.id.v_errorVertical);
        return v_errorVertical;
    }

    private AppTextView getErrorMessageHorizontal() {
        if (tv_errorHorizontal == null)
            tv_errorHorizontal = (AppTextView) v_parent.findViewById(R.id.tv_errorTextHorizontal);

        return tv_errorHorizontal;
    }

    private AppButton getRetryHorizontal() {
        if (btn_retryHorizontal == null)
            btn_retryHorizontal = (AppButton) v_parent.findViewById(R.id.btn_retryHorizontal);

        return btn_retryHorizontal;
    }

    private AppTextView getErrorMessageVertical() {
        if (tv_errorVertical == null)
            tv_errorVertical = (AppTextView) v_parent.findViewById(R.id.tv_errorVertical);

        return tv_errorVertical;
    }

    private AppButton getRetryVertical() {
        if (btn_retryVertical == null)
            btn_retryVertical = (AppButton) v_parent.findViewById(R.id.btn_retryVertical);

        return btn_retryVertical;
    }

}
