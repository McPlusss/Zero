package cn.mcplus.command

import cn.mcplus.Zero
import cn.mcplus.data.Config
import cn.mcplus.data.Regex
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.plugin.jvm.reloadPluginConfig
import net.mamoe.mirai.console.plugin.jvm.reloadPluginData

object Reload : SimpleCommand(
    Zero.INSTANCE, "reload", "重载",
    description = "重载插件配置文件以及数据文件"
) {
    @Handler
    suspend fun CommandSender.handle() {
        Zero.INSTANCE.reloadPluginConfig(Config)
        Zero.INSTANCE.reloadPluginData(Regex)
    }
}