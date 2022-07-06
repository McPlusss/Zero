package cn.mcplus.regex;

import cn.mcplus.Zero;
import cn.mcplus.data.Config;
import cn.mcplus.data.Regex;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static void handle(String message, String origin,
                              @Nullable GroupMessageEvent gme, @Nullable JSONObject data) {
        // 遍历正则物体
        Regex.INSTANCE.getItems().forEach(Item -> {
            // 判断来源是否一样
            if (Item.getOrigin().equals(origin)) {
                // 正则
                Pattern pattern = Pattern.compile(Item.getExpression());
                Matcher matcher = pattern.matcher(message);
                // 匹配
                if (matcher.find()) {
                    // 遍历输出
                    Item.getOutputs().forEach(output -> {
                        // 判断是否输出到群
                        if (output.getTarget().equals("Group") ) {
                            // 判断是否需要管理员权限
                            if (output.getAdmin()) {
                                // 判断消息是否来自群聊
                                if (Item.getOrigin().equals("Group") && gme != null) {
                                    // 判断是否存在于管理员列表当中
                                    if (Config.INSTANCE.getGroup().getAdmins().contains(gme.getSender().getId())) {
                                        // 占位符替换
                                        String outputMessage = replacePlaceholderWithData(output.getMessage(), data);
                                        // 发送消息
                                        sendMessage(outputMessage);
                                    }
                                }
                            } else {
                                // 占位符替换
                                String outputMessage = replacePlaceholderWithData(output.getMessage(), data);
                                // 发送消息
                                sendMessage(outputMessage);
                            }
                        }
                    });
                }
            }
        });
    }

    // 替换输出的占位符 --> Data
    public static String replacePlaceholderWithData(String output, @Nullable JSONObject data) {
        if (data == null) return output;

        String regExp = "\\{[A-Za-z0-9_\\.]+\\}";
        Matcher matcher = Pattern.compile(regExp).matcher(output);

        while (matcher.find()) {
            // 占位符
            String placeholder = matcher.group();
            // 占位符除去{}
            String inner = matcher.group().substring(1, matcher.group().length() - 1);
            // 占位符分割.
            String[] innerArray = inner.split("\\.");
            Zero.INSTANCE.getLogger().info(JSONObject.toJSONString(innerArray));
            // 位置
            JSONObject currentObject = data;
            // 是否位列表
            JSONArray currentArray = null;
            // 遍历
            for (String innerText : innerArray) {
                // 获取是否是Object
                if (currentObject != null && currentObject.containsKey(innerText)) {
                    // 获取类型
                    Class<?> innerClass = currentObject.get(innerText).getClass();
                    // 判断是否JSONObject
                    if (innerClass.equals(JSONObject.class)) {
                        currentObject = currentObject.getJSONObject(innerText);
                    } else if (innerClass.equals(JSONArray.class)) { // 判断是否JSONArray
                        currentArray = currentObject.getJSONArray(innerText);
                        currentObject = null;
                    } else {
                        // 输出
                        output = output.replace(placeholder, (String) currentObject.get(innerText));
                    }
                } else if (currentArray != null) { // 判断是否JSONArray
                    int i = Integer.parseInt(innerText); // 转数值
                    // 获取类型
                    Class<?> innerClass = currentArray.get(i).getClass();
                    // 判断是否JSONObject
                    if (innerClass.equals(JSONObject.class)) {
                        currentObject = currentArray.getJSONObject(i);
                        currentArray = null;
                    } else if (innerClass.equals(JSONArray.class)) { // 判断是否JSONArray
                        currentArray = currentArray.getJSONArray(i);
                    } else {
                        // 输出
                        output = output.replace(placeholder, (String) currentArray.get(i));
                    }
                }
            }
        }
        return output;
    }

    public static void handle(String message, String origin, JSONObject data) {
        handle(message, origin, null, data);
    }

    public static void sendMessage(String message) {
        // 获取聊天群号
        long chatGroup = Config.INSTANCE.getGroup().getChat();
        // 发送消息
        Objects.requireNonNull(
                Bot.getInstances().get(0).getGroup(chatGroup)
        ).sendMessage(message);
    }
}
