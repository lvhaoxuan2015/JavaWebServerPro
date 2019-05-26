package server.event;

import java.net.Socket;
import server.Request;

public class HTTPConnectEvent extends JWSPEvent {

    public Request request;
    public Socket socket;

    public HTTPConnectEvent() {
        this.eventType = "HTTPConnectEvent";
    }

    public HTTPConnectEvent(Socket socket, Request request) {
        this.eventType = "HTTPConnectEvent";
        this.socket = socket;
        this.request = request;
    }
}
