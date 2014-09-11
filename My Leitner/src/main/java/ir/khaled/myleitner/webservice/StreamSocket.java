package ir.khaled.myleitner.webservice;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import ir.khaled.myleitner.library.Util;

/**
 * Created by khaled on 9/11/2014.
 */
public class StreamSocket extends Socket {
    private OutputStreamWriter streamWriter;
    private InputStreamReader streamReader;

    public StreamSocket() throws IOException {
        connect(new InetSocketAddress(Util.SERVER_ADDRESS, Util.SERVER_PORT), 5000);
    }

    public OutputStreamWriter getStreamWriter() throws IOException {
        if (streamWriter == null)
            streamWriter = new OutputStreamWriter(getOutputStream());
        return streamWriter;
    }

    public InputStreamReader getStreamReader() throws IOException {
        if (streamReader == null)
            streamReader = new InputStreamReader(getInputStream());
        return streamReader;
    }
    
    public boolean isOpen() throws IOException {
        return Stack.ping(getStreamWriter());
    }
}
