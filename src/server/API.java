package server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.event.HTTPConnectEvent;

public class API {

    public static void respond(Socket socket, WebServer webServer) throws IOException {
        try {
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            if (request.isReal()) {
                if (webServer.JWSPeventManager.callEvent(new HTTPConnectEvent(socket, request))) {
                    SelfLogger.info(request.getRequestType() + " " + request.getRequestContent() + " " + socket.getInetAddress().getHostAddress());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        socket.close();
    }

    public static void write(OutputStream os, Response re) {
        try {
            os.write(re.toString().getBytes());
            os.write(re.getRequestByte());
        } catch (UnsupportedEncodingException ex) {
        } catch (IOException ex) {
        }
    }

    public static byte[] readFileToByte(File fileObject) {
        byte[] buffer = null;
        try {
            ByteArrayOutputStream bos;
            try (FileInputStream fis = new FileInputStream(fileObject)) {
                bos = new ByteArrayOutputStream(1000);
                byte[] b = new byte[1000];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
            }
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return buffer;
    }

    public static String readToString(File file) {
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                fileInputStream.read(fileContent);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return new String(fileContent);
    }

    public static String getType(String requestContent) {
        String s = requestContent.substring(requestContent.lastIndexOf(".") + 1, requestContent.length());
        if (s.equalsIgnoreCase("html") || s.equalsIgnoreCase("js") || s.equalsIgnoreCase("css")) {
            return "text/html";
        } else if (s.equalsIgnoreCase("jpg") || s.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        return "";
    }
}
