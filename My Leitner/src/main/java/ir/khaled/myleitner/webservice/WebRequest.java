package ir.khaled.myleitner.webservice;

import android.os.Handler;

import java.io.IOException;

import ir.khaled.myleitner.interfaces.ResponseListener;

/**
 * Created by khaled on 9/11/2014.
 */
public class WebRequest<T> extends Thread {
    static Handler handler = new Handler();
    Request request;
    ResponseListener<T> responseListener;

    public WebRequest(Request request, ResponseListener<T> responseListener) {
        this.request = request;
        this.responseListener = responseListener;
    }

    @Override
    public void run() {
        try {
            Response<T> response = Network.performRequest(request);

            if (response == null || !response.success) {
                sendError(response);
                return;
            }

            sendResponse(response.result);

        } catch (IOException e) {
            e.printStackTrace();
            sendError(null);
        }
    }

    private void sendError(final Response<T> response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                responseListener.onResponseError(response);
            }
        });
    }

    private void sendResponse(final T result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                responseListener.onResponse(result);
            }
        });
    }
}
