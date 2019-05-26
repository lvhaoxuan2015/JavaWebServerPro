package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.event.HTTPConnectEvent;

public class API {

    public static void respond(Socket socket, WebServer webServer) throws IOException {
        try {
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            if (!webServer.JWSPeventManager.callEvent(new HTTPConnectEvent(socket, request))) {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        socket.close();
    }
}
