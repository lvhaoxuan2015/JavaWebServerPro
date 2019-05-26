package server;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Request {

    private String requestType;
    private String requestContent;
    private String head;
    private String requestByte;

    public Request(InputStream inputStream) {
        byte[] data = new byte[1024];
        try {
            int len = inputStream.read(data);
            if (len != -1) {
                requestType = getRequestType0(data);
                requestContent = getRequestContent0(data);
                head = getHead0(data);
                requestByte = getRequestByte0(data);
            }
        } catch (IOException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setHead(String Head) {
        this.head = Head;
    }

    public void setRequestByte(String RequestByte) {
        this.requestByte = RequestByte;
    }

    public void setRequestContent(String RequestContent) {
        this.requestContent = RequestContent;
    }

    public void setRequestType(String RequestType) {
        this.requestType = RequestType;
    }

    public String getHead() {
        return head;
    }

    public String getRequestByte() {
        return requestByte;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public String getRequestType() {
        return requestType;
    }

    private String getRequestByte0(byte[] data) {
        String s = new String(data);
        int a = s.lastIndexOf("\r\n");
        int b = s.length() - 1;
        return s.substring(a + 1, b);
    }

    private String getHead0(byte[] data) {
        String s = new String(data);
        int a = s.indexOf("\r\n");
        int b = s.lastIndexOf("\r\n");
        return s.substring(a + 1, b);
    }

    private String getRequestType0(byte[] data) {
        String s = new String(data);
        int a = s.indexOf(" ");
        return s.substring(0, a);
    }

    @Override
    public String toString() {
        return requestType + " " + requestContent + " " + "HTTP/1.1" + "" + head + "\n" + requestByte + "\n";
    }

    private String getRequestContent0(byte[] data) {
        String s = new String(data);
        int a = s.indexOf(" ");
        int b = s.indexOf(" ", a + 1);
        int c = s.indexOf("?");
        if (!s.substring(a + 1, b).contains("?")) {
            return s.substring(a + 1, b).replace("/", "\\");
        } else {
            return s.substring(a + 1, c).replace("/", "\\");
        }
    }
}
