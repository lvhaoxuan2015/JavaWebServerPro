package server.event;

import java.net.Socket;

public class SocketConnectEvent extends JWSPEvent {

    public Socket socket;

    public SocketConnectEvent() {
        this.eventType = "SocketConnectEvent";
    }

    public SocketConnectEvent(Socket socket) {
        this.eventType = "SocketConnectEvent";
        this.socket = socket;
    }
}
