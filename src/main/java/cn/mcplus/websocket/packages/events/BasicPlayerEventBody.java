package cn.mcplus.websocket.packages.events;

import cn.mcplus.server.bedrock.object.Player;

public class BasicPlayerEventBody extends BasicEventBody {
    Player player;

    public Player getPlayer() {
        return player;
    }
}
