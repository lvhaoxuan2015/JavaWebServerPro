package fileshare.event;

import fileshare.FileShare;
import fileshare.user.User;
import fileshare.user.UserManager;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.API;
import server.JWSPro;
import server.Request;
import server.Response;
import server.WebServer;
import server.event.HTTPConnectEvent;
import server.event.JWSPEvent;

public class HTTPConnectEventExtends extends HTTPConnectEvent {

    public static File workPath = FileShare.ins.getDataFolder();
    public static String path_404 = "404.html";
    public static String path_main = "main.html";
    public static String path_successful = "successful.html";
    public static String path_faild = "failed.html";

    @Override
    public void call(JWSPEvent event) {
        HTTPConnectEvent realEvent = (HTTPConnectEvent) event;
        if (realEvent.request.getRequestType().equals("GET")) {
            respondGet(realEvent.request, JWSPro.webServer, realEvent.socket);
        } else if (realEvent.request.getRequestType().equals("POST")) {
            respondPOST(realEvent.request, JWSPro.webServer, realEvent.socket);
        }
    }

    public void respondGet(Request request, WebServer webServer, Socket socket) {
        String requestContent = request.getRequestContent();
        if (requestContent.equalsIgnoreCase("\\")) {
            File file = new File(workPath, requestContent);
            if (file.exists()) {
                try {
                    OutputStream os = socket.getOutputStream();
                    List<String> head = new ArrayList<>();
                    head.add("Content-Type: %TYPE\r\n".replace("%TYPE", API.getType(path_main)));
                    if (request.getRequestCookieMap().containsKey("loginLnfo_JWSPro") && UserManager.cockieUsers.containsKey(request.getRequestCookieMap().get("loginLnfo_JWSPro"))) {
                        Response re = new Response("HTTP/1.1", "200", "OK", head, API.readToString(new File(workPath, path_main)).replace("%USER%", UserManager.cockieUsers.get(request.getRequestCookieMap().get("loginLnfo_JWSPro")).getName()).getBytes());
                        API.write(os, re);
                    } else {
                        Response re = new Response("HTTP/1.1", "200", "OK", head, API.readToString(new File(workPath, path_main)).replace("%USER%", "no login").getBytes());
                        API.write(os, re);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(HTTPConnectEventExtends.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    showFile(socket.getOutputStream(), new File(workPath, path_404));
                } catch (IOException ex) {
                    Logger.getLogger(HTTPConnectEventExtends.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            File file = new File(workPath, requestContent);
            if (file.exists()) {
                try {
                    OutputStream os = socket.getOutputStream();
                    List<String> head = new ArrayList<>();
                    head.add("Content-Type: %TYPE\r\n".replace("%TYPE", API.getType(requestContent)));
                    if (request.getRequestCookieMap().containsKey("loginLnfo_JWSPro") && UserManager.cockieUsers.containsKey(request.getRequestCookieMap().get("loginLnfo_JWSPro"))) {
                        Response re = new Response("HTTP/1.1", "200", "OK", head, API.readToString(new File(workPath, requestContent)).replace("%USER%", UserManager.cockieUsers.get(request.getRequestCookieMap().get("loginLnfo_JWSPro")).getName()).getBytes());
                        API.write(os, re);
                    } else {
                        Response re = new Response("HTTP/1.1", "200", "OK", head, API.readToString(new File(workPath, requestContent)).replace("%USER%", "no login").getBytes());
                        API.write(os, re);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(HTTPConnectEventExtends.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    showFile(socket.getOutputStream(), new File(workPath, path_404));
                } catch (IOException ex) {
                    Logger.getLogger(HTTPConnectEventExtends.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void respondPOST(Request request, WebServer webServer, Socket socket) {
        User user = new User(request.getRequestParamMap().get("user"), request.getRequestParamMap().get("password"));
        if (request.getRequestContent().equals("\\login.html") && UserManager.login(user)) {
            File file = new File(workPath, path_successful);
            if (file.exists()) {
                try {
                    OutputStream os = socket.getOutputStream();
                    List<String> head = new ArrayList<>();
                    head.add("Content-Type: text/html\r\n");
                    head.add("Location: " + path_successful + "\r\n");
                    head.add("Set-Cookie: loginLnfo_JWSPro=" + user.getCockie() + "; path=/;");
                    Response re = new Response("HTTP/1.1", "302", "OK", head, API.readFileToByte(file));
                    API.write(os, re);
                } catch (IOException ex) {
                    Logger.getLogger(HTTPConnectEventExtends.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    showFile(socket.getOutputStream(), new File(workPath, path_404));
                } catch (IOException ex) {
                    Logger.getLogger(HTTPConnectEventExtends.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (request.getRequestContent().equals("\\register.html") && UserManager.register(user)) {
            File file = new File(workPath, path_successful);
            if (file.exists()) {
                try {
                    OutputStream os = socket.getOutputStream();
                    List<String> head = new ArrayList<>();
                    head.add("Content-Type: text/html\r\n");
                    head.add("Location: " + path_successful + "\r\n");
                    head.add("Set-Cookie: loginLnfo_JWSPro=" + user.getCockie() + "; path=/;");
                    Response re = new Response("HTTP/1.1", "302", "OK", head, API.readFileToByte(file));
                    API.write(os, re);
                } catch (IOException ex) {
                    Logger.getLogger(HTTPConnectEventExtends.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    showFile(socket.getOutputStream(), new File(workPath, path_404));
                } catch (IOException ex) {
                    Logger.getLogger(HTTPConnectEventExtends.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            try {
                showFile(socket.getOutputStream(), new File(workPath, path_faild));
            } catch (IOException ex) {
                Logger.getLogger(HTTPConnectEventExtends.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void showFile(OutputStream os, File file) {
        if (file.exists()) {
            List<String> head = new ArrayList<>();
            head.add("Content-Type: text/html\r\n");
            Response re = new Response("HTTP/1.1", "200", "OK", head, API.readFileToByte(file));
            API.write(os, re);
        } else {
            List<String> head = new ArrayList<>();
            head.add("Content-Type: text/html\r\n");
            Response re = new Response("HTTP/1.1", "200", "OK", head, API.readFileToByte(new File(workPath, path_404)));
            API.write(os, re);
        }
    }
}
