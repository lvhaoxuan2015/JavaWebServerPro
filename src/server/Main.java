package server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        JWSPro.webServer = new WebServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        JWSPro.webServer.start();
    }
}
