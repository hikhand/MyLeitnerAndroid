package ir.khaled.myleitner.webservice;

import java.io.IOException;

import ir.khaled.myleitner.helper.Errors;
import ir.khaled.myleitner.model.Device;

/**
 * Created by khaled on 9/11/2014.
 */
public class Network {
    private static StreamSocket socketPermanent;

    private Network() {
    }

    public static <T> Response<T> performRequest(Request request) throws IOException {
        return performRequestImp(request, getSocket(request));
    }

    private static <T> Response<T> performRequestImp(Request request, StreamSocket streamSocket) throws IOException {
        Response<T> response = Stack.performRequest(streamSocket, request);

        if (response == null)
            return null;

        if (response.success)
            return response;

        switch (response.errorCode) {
            case Errors.UNKNOWN_DEVICE:
                performRequestImp(Device.getRequestRegisterDevice(), streamSocket);
                return performRequestImp(request, streamSocket);
        }

        return response;
    }

    private static StreamSocket getSocket(Request request) throws IOException {
        switch (request.connection) {
            case IMMEDIATE:
                return new StreamSocket();

            case PERMANENT:
                return getSocketPermanent();

            default:
                return null;
        }
    }

    private static StreamSocket getSocketPermanent() throws IOException {
        if (socketPermanent == null || !socketPermanent.isOpen())
            socketPermanent = new StreamSocket();

        return socketPermanent;
    }
}
