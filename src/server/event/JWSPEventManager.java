package server.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JWSPEventManager {

    public HashMap<String, List<JWSPEvent>> JWSPEventMap = new HashMap<>();

    public void registerEvent(JWSPEvent event) {
        if (!JWSPEventMap.containsKey(event.eventType)) {
            JWSPEventMap.put(event.eventType, new ArrayList<>());
        }
        JWSPEventMap.get(event.eventType).add(event);
    }

    public boolean callEvent(JWSPEvent callEvent) {
        boolean ret = false;
        if (JWSPEventMap.containsKey(callEvent.eventType)) {
            for (JWSPEvent event : JWSPEventMap.get(callEvent.eventType)) {
                event.call(callEvent);
                ret = event.isCancel;
            }
        }
        return !ret;
    }
}
