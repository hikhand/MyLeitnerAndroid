package ir.khaled.myleitner.interfaces;

import ir.khaled.myleitner.webservice.Response;

/**
 * Created by kh.bakhtiari on 5/29/2014.
 */
public interface ResponseListener<T> {

    /**
     * @param response may be null
     */
    public void onResponse(T response);

    /**
     * @param response may be null
     */
    public void onResponseError(Response<T> response);
}
