package server;

public class Main {

    public static void main(String[] args) {
        JWSPro.webServer = new WebServer(80, 100);
        JWSPro.webServer.start();
    }

}
