package ir.khaled.myleitner.interfaces;

import android.view.View;

/**
 * Created by kh.bakhtiari on 7/1/2014.
 */
public interface Loading {

    public void startLoading();

    public void stopLoading();

    public void showError(String errorMessage, String textButton, View.OnClickListener onClickListener);

}
