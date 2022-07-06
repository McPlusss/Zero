package cn.mcplus.websocket;

import cn.mcplus.Zero;
import cn.mcplus.data.Config;
import cn.mcplus.regex.Utils;
import cn.mcplus.websocket.packages.AuthenticationBody;
import cn.mcplus.websocket.packages.AuthenticationPackage;
import cn.mcplus.websocket.packages.BasicPackage;
import cn.mcplus.websocket.packages.Header;
import cn.mcplus.websocket.packages.events.*;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server extends WebSocketServer {

    Map<String, List<WebSocket>> unauthorized = new HashMap<>();
    Map<String, List<WebSocket>> authorized = new HashMap<>();

    public Server(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        // 获取ws连接地址
        String address = conn.getRemoteSocketAddress().getAddress().getHostAddress();

        Zero.INSTANCE.getLogger().info("客户端连接, 地址 " + address);

        // 判断是否已经认证
        if (authorized.containsKey(address)) {
            Zero.INSTANCE.getLogger().info(String.format("客户端 %s 已认证", address));
            return;
        }

        // 判断是否已经存在未认证列表
        if (unauthorized.containsKey(address)) {
            unauthorized.get(address).add(conn);
        } else {
            List<WebSocket> newList = new ArrayList<>();
            newList.add(conn);
            unauthorized.put(address, newList);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // 获取ws连接地址
        String address = conn.getRemoteSocketAddress().getAddress().getHostAddress();

        Zero.INSTANCE.getLogger().info("客户端断开, 地址: " + address);

        if (authorized.containsKey(address)) {
            if (authorized.get(address).size() == 1) {
                authorized.remove(address);
            } else {
                authorized.get(address).remove(conn);
            }
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // 获取ws连接地址
        String address = conn.getRemoteSocketAddress().getAddress().getHostAddress();

        Zero.INSTANCE.getLogger().info(String.format("客户端 %s 发送消息: %s", address, message));

        // Gson实例化
        Gson gson = new Gson();

        // Json转基础包
        BasicPackage basicPackage = gson.fromJson(message, BasicPackage.class);
        String messagePurpose = basicPackage.getHeader().getMessagePurpose();

        // 判断是否已经认证
        if (!authorized.containsKey(address)) {
            // 判断是否为认证包
            if (messagePurpose.equals("authenticate")) {
                //认证状态
                String result = "unauthorized";

                // Json转为认证包
                AuthenticationPackage authenticationPackage = gson.fromJson(message, AuthenticationPackage.class);
                // 判断密钥是否正确
                String secretKey = authenticationPackage.getBody().getSecretKey();

                if (secretKey.equals(Config.INSTANCE.getWebSocket().getSecretKey())) {
                    // 判断是否已经认证
                    if (authorized.containsKey(address)) {
                        authorized.get(address).add(conn);
                    } else {
                        List<WebSocket> newList = new ArrayList<>();
                        newList.add(conn);
                        authorized.put(address, newList);
                    }

                    result = "authorized";
                }
                // 发送结果
                AuthenticationPackage resultPackage = new AuthenticationPackage(
                        new Header(result, authenticationPackage.getHeader().getServer()),
                        new AuthenticationBody(secretKey)
                );
                String resultJson = gson.toJson(resultPackage);

                conn.send(resultJson);
            }
        } else {
            if (messagePurpose.equals("event")) {
                // 转为基础事件包
                BasicEventPackage basicEventPackage = gson.fromJson(message, BasicEventPackage.class);
                // 获取事件名称
                String eventName = basicEventPackage.getBody().getEventName();
                if (eventName.equals("PlayerMessage")) {
                    // 转为玩家消息事件包
                    PlayerMessageEventPackage playerMessageEventPackage = gson.fromJson(message,
                            PlayerMessageEventPackage.class);
                    String playerMessage = playerMessageEventPackage.getBody().getMessage();
                    PlayerMessageEventBody body = playerMessageEventPackage.getBody();
                    String json = gson.toJson(body);
                    Utils.handle(playerMessage, "PlayerMessage", JSONObject.parseObject(json));
                } else if (eventName.equals("PlayerJoin")) { // 玩家加入事件
                    PlayerJoinEventPackage playerJoinEventPackage = gson.fromJson(message,
                            PlayerJoinEventPackage.class);
                    BasicPlayerEventBody body = playerJoinEventPackage.getBody();
                    String json = gson.toJson(body);
                    Utils.handle("", "PlayerJoin", JSONObject.parseObject(json));
                } else if (eventName.equals("PlayerLeft")) {
                    PlayerLeftEventPackage playerLeftEventPackage= gson.fromJson(message,
                            PlayerLeftEventPackage.class);
                    BasicPlayerEventBody body = playerLeftEventPackage.getBody();
                    String json = gson.toJson(body);
                    Utils.handle("", "PlayerLeft", JSONObject.parseObject(json));
                }
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    @Override
    public void onStart() {

    }
}
