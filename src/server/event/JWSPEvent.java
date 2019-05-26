package server.event;

public class JWSPEvent {

    public String eventType = "JWSPEvent";
    public boolean isCancel = false;

    public void call(JWSPEvent event) {
    }

    public void setIsCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }
}
