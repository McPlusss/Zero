package cn.mcplus.websocket.packages.events;

import cn.mcplus.websocket.packages.BasicPackage;

public class PlayerMessageEventPackage extends BasicPackage {
    PlayerMessageEventBody body;

    public PlayerMessageEventBody getBody() {
        return body;
    }
}
