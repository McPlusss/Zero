package cn.mcplus.data

import cn.mcplus.regex.Output
import cn.mcplus.regex.RegexItem
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object Regex : AutoSavePluginData("RegExp") {
    val Items : List<RegexItem> by value(listOf(RegexItem()))
}