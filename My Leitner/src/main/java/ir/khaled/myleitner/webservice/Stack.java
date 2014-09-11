package ir.khaled.myleitner.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by khaled on 9/11/2014.
 */
public class Stack {
    private static final String EOF = "\u001a\uFFFF\u001A\uFFFF";
    private static final int LEN_EOF = EOF.length();
    private static Gson gson;

    private Stack() {
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public static <T> Response<T> performRequest(StreamSocket streamSocket, Request request) throws IOException {
        String jsonRequest = request.getJsonRequest();

        synchronized (streamSocket) {
            sendRequest(streamSocket.getStreamWriter(), jsonRequest);

            return getResponse(streamSocket.getStreamReader(), request);
        }
    }

    private static void sendRequest(OutputStreamWriter streamWriter, String jsonRequest) throws IOException {
        jsonRequest += EOF;
        streamWriter.write(jsonRequest);
        streamWriter.flush();
    }

    public static boolean ping(OutputStreamWriter streamWriter) {
        try {
            sendRequest(streamWriter, "");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static <T> Response<T> getResponse(InputStreamReader streamReader, Request request) throws IOException {
        char[] buffer = new char[8192];
        String part, result = "";
        int length = 0;
        while ((length = streamReader.read(buffer)) != -1) {
            part = new String(buffer, 0, length);
            if (length >= LEN_EOF && part.substring(length - LEN_EOF).equals(EOF)) {//if the outputStream is ended
                part = part.substring(0, length - LEN_EOF);
                result += part;
                break;
            }
            result += part;
        }
        return getGson().fromJson(result, request.type);
    }

    private static Gson getGson() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.excludeFieldsWithoutExposeAnnotation();
            gson = gsonBuilder.create();
        }
        return gson;
    }

}
