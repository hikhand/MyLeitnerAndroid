package ir.khaled.myleitner.helper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by kh.bakhtiari on 5/9/2014.
 */
public class SocketHelper {
    private static SocketHelper ourInstance;
    private Socket socket;

    /**
     * opens a socket connection to server
     */
    private SocketHelper() {
        try {
            socket = new Socket(Util.SERVER_ADDRESS, Util.SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return an instance of {@link ir.khaled.myleitner.helper.SocketHelper} to interact with server.
     */
    public static SocketHelper getConnection() {
        if (ourInstance == null || ourInstance.socket == null || !ourInstance.socket.isConnected() || ourInstance.socket.isClosed())
            ourInstance = new SocketHelper();

        return ourInstance;
    }

    public synchronized String getData(String jsonToSend) {
        String result = "";
        try {
//            BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
//            outputStream.write(jsonToSend.getBytes(), 0, jsonToSend.getBytes().length);

            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
            jsonToSend += "\r\n";
            writer.write(jsonToSend, 0, jsonToSend.length());
            writer.flush();

            InputStreamReader reader = new InputStreamReader(socket.getInputStream());


            int length = 0;
            char[] buffer = new char[8192];
            String part;
            while ((length = reader.read(buffer)) != -1) {
                part = new String(buffer, 0, length);
                if (part.contains("\r\n")) {
                    part = part.substring(0, part.length() - 1);
                    result += part;
                    break;
                }
                result += part;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ourInstance = null;//TODO maybe not !
        return result.length() == 0 ? null : result;
    }

}