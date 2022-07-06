package cn.mcplus.websocket.packages.events;

import cn.mcplus.websocket.packages.BasicPackage;

public class BasicPlayerEventPackage extends BasicPackage {
    BasicPlayerEventBody body;

    public BasicPlayerEventBody getBody() {
        return body;
    }
}
