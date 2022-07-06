package cn.mcplus;

import cn.mcplus.command.Reload;
import cn.mcplus.data.Config;
import cn.mcplus.data.Regex;
import cn.mcplus.listener.GroupMessage;
import cn.mcplus.regex.Utils;
import cn.mcplus.websocket.Server;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

public final class Zero extends JavaPlugin {
    public static final Zero INSTANCE = new Zero();

    private Zero() {
        super(new JvmPluginDescriptionBuilder("cn.mcplus.zero", "1.0.0")
                .name("Zero")
                .author("McPlus")
                .build());
    }

    @Override
    public void onEnable() {
        // 重载配置文件
        reloadPluginConfig(Config.INSTANCE);
        // 重载正则数据
        reloadPluginData(Regex.INSTANCE);

        // 订阅事件
        subscribeEvents();

        // 启动ws服务器
        startWebSocketServer();

        // 注册命令
        CommandManager.INSTANCE.registerCommand(Reload.INSTANCE, true);

        // 检查配置文件
        checkConfig();

        getLogger().info("插件加载成功!");
    }

    void checkConfig() {
        if (Config.INSTANCE.getServer().get("Path").equals("")) {
            getLogger().warning("服务端路径尚未配置，可能会影响机器人使用！");
        }

        if (Config.INSTANCE.getWebSocket().getSecretKey().equals("")) {
            getLogger().warning("WebSocket密钥尚未配置，可能会影响机器人使用！");
        }
    }

    void subscribeEvents() {
        GroupMessage.addHandle(e -> {
            long groupid = e.getGroup().getId();
            if (Config.INSTANCE.getGroup().getMain() == groupid || Config.INSTANCE.getGroup().getChat() == groupid) {
                String message = e.getMessage().contentToString();
                Utils.handle(message, "Group", e, null);
            }
        });

        GroupMessage.subscribe();
    }

    void startWebSocketServer() {
        Server server = new Server(Config.INSTANCE.getWebSocket().getPort());
        server.start();
        getLogger().info("服务器端启动, 端口号为：" + server.getPort());

    }
}