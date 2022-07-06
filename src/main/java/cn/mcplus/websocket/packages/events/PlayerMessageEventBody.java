package cn.mcplus.websocket.packages.events;

import cn.mcplus.server.bedrock.object.Player;

public class PlayerMessageEventBody extends BasicPlayerEventBody {
    String message;

    public String getMessage() {
        return message;
    }
}
