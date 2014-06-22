package ir.khaled.myleitner.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;

import ir.khaled.myleitner.helper.ErrorHelper;
import ir.khaled.myleitner.helper.Logger;
import ir.khaled.myleitner.helper.Util;
import ir.khaled.myleitner.interfaces.ResponseReceiveListener;

/**
 * Created by kh.bakhtiari on 5/29/2014.
 */
public class WebClient<T> extends Thread {
    private static final String EOF = "\u001a\uFFFF\u001A\uFFFF";
    private static final int TIME_OUT_CONNECT = 5 * 1000;
    private static final int TIME_OUT_READ = 30 * 1000;
    private static Socket socketOpen;
    private static WebRequest requestPing;
    private static Gson gson;
    private Socket socket;
    private WebRequest request;
    private ResponseReceiveListener<T> receiveListener;
    private Connection connectionType;
    private Type typeResult;
    private Context context;

    /**
     * @param request
     * @param connectionType
     * @param typeResult       Type myType = new TypeToken<WebResponse<yourClass>>() {}.getType();
     * @param receiveListener
     */
    public WebClient(Context context, WebRequest request, Connection connectionType, Type typeResult, ResponseReceiveListener<T> receiveListener) {
        this.request = request;
        this.receiveListener = receiveListener;
        this.connectionType = connectionType;
        this.typeResult = typeResult;
        this.context = context;
        if (requestPing == null)
            requestPing = new WebRequest(context, WebRequest.REQUEST_PING);
    }


    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            defineSocketConnection();
            getData(socket, request.getJsonParams(), true);
            Log.i("Speed", request.requestName + " in: " + (System.currentTimeMillis() - start) + " millis");
        } catch (Exception e) {
            e.printStackTrace();
            receiveListener.onResponseReceiveFailed(null);
        }
    }

    private boolean handleResponse(WebResponse<T> response, boolean returnToListener) throws IOException {
        if (returnToListener && response.success) {
            receiveListener.onResponseReceived(response.result);
            return true;
        }

        if (response.success)
            return true;

        switch (response.errorCode) {
            case ErrorHelper.UNKNOWN_DEVICE:
                if (getData(socket, Device.getRequestRegisterDevice(context).getJsonParams(), false)) {
                    return getData(socket, request.getJsonParams(), true);
                }
            default:
                if (returnToListener) {
                    receiveListener.onResponseReceiveFailed(response);
                }
                return false;
        }
    }

    /**
     * if the connection is set to be closed immediately closes the connection.
     */
    private void closeConnection() throws IOException {
        if (connectionType == Connection.IMMEDIATELY)
            socket.close();
    }

    private boolean getData(Socket socket, String jsonToSend,  boolean returnToListener) throws IOException {
        if (connectionType == Connection.IMMEDIATELY) {
            WebResponse<T> response = getDataB(socket, jsonToSend);
            closeConnection();
            return handleResponse(response, returnToListener);
        } else {
            synchronized (WebClient.class) {
                WebResponse<T> response = getDataB(socket, jsonToSend);
                return handleResponse(response, returnToListener);
            }
        }
    }

    private WebResponse<T> getDataB(Socket socket, String jsonToSend) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
        jsonToSend += EOF;
        writer.write(jsonToSend);
        writer.flush();

        InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
        return getResponse(streamReader);
    }

    private WebResponse<T> getResponse(InputStreamReader streamReader) throws IOException {
        char[] buffer = new char[8192];
        String part, result = "";
        int length = 0;
        while ((length = streamReader.read(buffer)) != -1) {
            part = new String(buffer, 0, length);
            if (length >= 4 && part.substring(length - 4).equals(EOF)) {//if the outputStream is ended
                part = part.substring(0, length - 4);
                result += part;
                break;
            }
            result += part;
        }
        return getGson().fromJson(result, typeResult);
    }

    private Gson getGson() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.excludeFieldsWithoutExposeAnnotation();
            gson = gsonBuilder.create();
        }
        return gson;
    }

    private Socket getNewSocket() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(Util.SERVER_ADDRESS, Util.SERVER_PORT), TIME_OUT_CONNECT);
//        socket.setSoTimeout(TIME_OUT_READ);//TODO uncomment this part.
        return socket;
    }

    private void defineSocketConnection() throws IOException {
        if (connectionType == Connection.IMMEDIATELY) {
            socket = getNewSocket();
        } else socket = getSocketOpen();
    }

    private Socket getSocketOpen() throws IOException {
        synchronized (WebClient.class) {
            if (socketOpen != null && !pingServer()) {
                socketOpen = getNewSocket();
            } else if (socketOpen == null) {
                socketOpen = getNewSocket();
            }

            return socketOpen;
        }
    }

    private boolean pingServer() {
        long pingStart = System.currentTimeMillis();
        try {
            OutputStreamWriter writer = new OutputStreamWriter(socketOpen.getOutputStream());
            writer.write(EOF);
            writer.flush();
            Logger.LogI("Speed", "ping time: " + (System.currentTimeMillis() - pingStart));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.LogI("Speed", "ping failed time: " + (System.currentTimeMillis() - pingStart));
        return false;
    }

    public static enum Connection {
        PERMANENT,
        IMMEDIATELY
    }
}