package ir.khaled.myleitner.Helper;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by kh.bakhtiari on 5/9/2014.
 */
public class SocketHelper {
    private static SocketHelper ourInstance;
    Socket socket;

    private SocketHelper() {
        try {
            socket = new Socket(Config.SERVER_ADDRESS, Config.SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SocketHelper getInstance() {
        if (ourInstance == null || ourInstance.socket == null || !ourInstance.socket.isConnected() || ourInstance.socket.isClosed())
            ourInstance = new SocketHelper();
        return ourInstance;
    }
}
