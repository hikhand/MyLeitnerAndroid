package ir.khaled.myleitner.interfaces;

import ir.khaled.myleitner.model.WebResponse;

/**
 * Created by kh.bakhtiari on 5/29/2014.
 */
public interface ResponseReceiveListener<T> {

    /**
     * @param response may be null
     */
    public void onResponseReceived(T response);

    /**
     * @param response may be null
     */
    public void onResponseReceiveFailed(WebResponse<T> response);
}
