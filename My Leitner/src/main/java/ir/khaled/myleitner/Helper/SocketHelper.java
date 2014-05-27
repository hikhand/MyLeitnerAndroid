package ir.khaled.myleitner.helper;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by kh.bakhtiari on 5/9/2014.
 */
public class SocketHelper {
    private static SocketHelper ourInstance;
//    private static ArrayList<IServerConnectionListener> connectionListeners;
    private Socket socket;

    /**
     * opens a socket connection to server
     */
    private SocketHelper() {
        try {
//            connectionListeners = new ArrayList<IServerConnectionListener>();
            socket = new Socket(Util.SERVER_ADDRESS, Config.SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return an instance of {@link ir.khaled.myleitner.helper.SocketHelper} to connect to server.
     */
    public static SocketHelper getConnection() {
        if (ourInstance == null || ourInstance.socket == null || !ourInstance.socket.isConnected() || ourInstance.socket.isClosed())
            ourInstance = new SocketHelper();

        return ourInstance;
    }

    /**
     * interface to determine whether its successful to connect to server or not
     */
    public static interface IServerConnectionListener {

        public void onConnectToServerSucceed();

        public void onConnectToServerFailed();

        public void onConnectionClosed();
    }
}