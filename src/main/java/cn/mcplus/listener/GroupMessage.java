package cn.mcplus.listener;

import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.ArrayList;
import java.util.List;

public class GroupMessage {
    private static final List<groupMessageHandle> handleList = new ArrayList<>();

    public interface groupMessageHandle {
        void run(GroupMessageEvent e);
    }

    public static boolean addHandle(groupMessageHandle handle) {
        return handleList.add(handle);
    }

    public static void subscribe() {
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, e -> {
            handleList.forEach(handle -> handle.run(e));
        });
    }
}
