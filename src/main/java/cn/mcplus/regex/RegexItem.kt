package cn.mcplus.regex

import kotlinx.serialization.Serializable

@Serializable
class RegexItem {
    var expression : String = ""
    var origin : String = "Group"
    var outputs : List<Output> = listOf(Output())
}