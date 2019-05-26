package server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketRunnable implements Runnable {

    public Socket socket;
    public WebServer webServer;

    public SocketRunnable(Socket socket, WebServer webServer) {
        this.socket = socket;
        this.webServer = webServer;
    }

    @Override
    public void run() {
        try {
            API.respond(socket, webServer);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
