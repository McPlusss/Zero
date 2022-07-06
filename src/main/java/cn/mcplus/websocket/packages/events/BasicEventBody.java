package cn.mcplus.websocket.packages.events;

import cn.mcplus.websocket.packages.BasicBody;

public class BasicEventBody extends BasicBody {
    String eventName;

    public String getEventName() {
        return eventName;
    }
}
