package server;

import server.plugin.PluginLoader;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.event.SocketConnectEvent;
import server.event.JWSPEventManager;

public class WebServer extends Thread {

    public ServerSocket serverSocket;
    public int port;
    public int maxThread;
    public JWSPEventManager JWSPeventManager;
    public PluginLoader pluginLoader;

    public WebServer(int port, int maxThread) {
        this.port = port;
        this.maxThread = maxThread;
        this.JWSPeventManager = new JWSPEventManager();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        if (serverSocket.isBound()) {
            long startTime = System.currentTimeMillis();
            SelfLogger.info("JavaWebServerPro is starting...");
            SelfLogger.info("load port: " + port);
            SelfLogger.info("max thread: " + maxThread);
            ExecutorService threadPool = Executors.newFixedThreadPool(maxThread);
            Socket socket;
            String path = System.getProperty("user.dir");
            pluginLoader = new PluginLoader(new File(path, "plugins"));
            pluginLoader.loadPlugins();
            long finishTime = System.currentTimeMillis();
            SelfLogger.info("Done! <" + (finishTime - startTime) * 1.0 / 1000 + "s>!");
            while (serverSocket.isBound()) {
                try {
                    socket = serverSocket.accept();
                    if (JWSPeventManager.callEvent(new SocketConnectEvent(socket))) {
                        threadPool.execute(new SocketRunnable(socket, this));
                    }
                } catch (IOException ex) {
                    Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
