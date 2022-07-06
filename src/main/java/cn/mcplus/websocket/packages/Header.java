package cn.mcplus.websocket.packages;

public class Header {
    String messagePurpose; // 消息用途
    String server; // 服务端

    public Header(String messagePurpose, String server) {
        this.messagePurpose = messagePurpose;
        this.server = server;
    }

    public String getMessagePurpose() {
        return messagePurpose;
    }

    public String getServer() {
        return server;
    }
}
