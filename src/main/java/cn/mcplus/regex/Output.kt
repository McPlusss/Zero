package cn.mcplus.regex

import kotlinx.serialization.Serializable

@Serializable
class Output {
    var target : String = "Group"
    var message : String = ""
    var admin : Boolean = false
}