package server;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Request {

    private String requestType;
    private String requestContent;
    private String head;
    private HashMap<String, String> requestHeadMap = new HashMap<>();
    private String requestByte;
    private HashMap<String, String> requestParamMap = new HashMap<>();
    private HashMap<String, String> requestCookieMap = new HashMap<>();

    public Request(InputStream inputStream) {
        byte[] data = new byte[1024];
        try {
            int len = inputStream.read(data);
            if (len != -1) {
                requestType = getRequestType0(data);
                requestContent = getRequestContent0(data);
                head = getHead0(data);
                for (String line : head.split("\r\n")) {
                    String[] params = line.split(": ");
                    if (params.length == 2) {
                        requestHeadMap.put(params[0], params[1]);
                    }
                }
                if (requestHeadMap.containsKey("Cookie")) {
                    for (String info : requestHeadMap.get("Cookie").split("; ")) {
                        String[] params = info.split("=");
                        if (params.length == 2) {
                            requestCookieMap.put(params[0], params[1]);
                        }
                    }
                }
                requestByte = getRequestByte0(data);
                for (String line : requestByte.split("&")) {
                    String[] params = line.split("=");
                    if (params.length == 2) {
                        requestParamMap.put(params[0], params[1]);
                    }
                }
            }
        } catch (IOException | StringIndexOutOfBoundsException ex) {
        }
    }

    public boolean isReal() {
        return requestType != null && requestContent != null && head != null && requestByte != null;
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

    public HashMap<String, String> getRequestParamMap() {
        return requestParamMap;
    }

    public HashMap<String, String> getRequestHeadMap() {
        return requestHeadMap;
    }

    public HashMap<String, String> getRequestCookieMap() {
        return requestCookieMap;
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
        if (requestHeadMap.get("Content-Length") != null) {
            String s = new String(data);
            int a = s.lastIndexOf("\r\n");
            int b = a + Integer.parseInt(requestHeadMap.get("Content-Length")) + 2;
            return s.substring(a + 2, b);
        }
        return "";
    }

    private String getHead0(byte[] data) {
        String s = new String(data);
        int a = s.indexOf("\r\n");
        int b = s.lastIndexOf("\r\n");
        return s.substring(a + 2, b);
    }

    private String getRequestType0(byte[] data) {
        String s = new String(data);
        int a = s.indexOf(" ");
        return s.substring(0, a);
    }

    @Override
    public String toString() {
        return (requestType + " " + requestContent + " " + "HTTP/1.1\r\n" + head + "\r\n" + requestByte);
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
