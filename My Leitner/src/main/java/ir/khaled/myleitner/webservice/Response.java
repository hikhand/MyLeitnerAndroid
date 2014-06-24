package ir.khaled.myleitner.webservice;

import com.google.gson.annotations.Expose;

/**
 * Created by kh.bakhtiari on 5/27/2014.
 */
public class Response<T> {
    @Expose public boolean success;
    @Expose public int errorCode;
    @Expose public String message;
    @Expose public String requestMethod;
    @Expose public T result;
}
