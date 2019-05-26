package server;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private String httpVersion;
    private String typeId;
    private String typeDescription;
    private List<String> head;
    private List<byte[]> requestByte = new ArrayList<>();

    public Response(String httpVersion, String typeId, String typeDescription, List<String> Head, List<byte[]> RequestByte) {
        this.httpVersion = httpVersion;
        this.typeId = typeId;
        this.typeDescription = typeDescription;
        this.head = Head;
        this.requestByte = RequestByte;
    }

    @Override
    public String toString() {
        String ret;
        ret = this.httpVersion + " " + this.typeId + " " + this.typeDescription + "\r\n";
        ret = head.stream().map((s) -> s).reduce(ret, String::concat);
        ret += "\r\n";
        return ret;
    }

    public void setHead(List<String> Head) {
        this.head = Head;
    }

    public void setRequestByte(List<byte[]> RequestByte) {
        this.requestByte = RequestByte;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public String getTypeId() {
        return typeId;
    }

    public List<String> getHead() {
        return head;
    }

    public List<byte[]> getRequestByte() {
        return requestByte;
    }
}
