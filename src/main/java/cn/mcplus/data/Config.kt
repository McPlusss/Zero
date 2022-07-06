package cn.mcplus.data

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
import kotlinx.serialization.Serializable
import javax.crypto.SecretKey

object Config: AutoSavePluginConfig("Config") {
    val Group : Group by value(Group())
    val Server : Map<String, String> by value(mapOf("Path" to ""))
    val WebSocket : WebSocket by value(WebSocket())
}

@Serializable
class Group {
    var Main : Long = 615056067
    var Chat : Long = 615056067
    var Admins : List<Long> = listOf(3147884580)
}

@Serializable
class WebSocket {
    var SecretKey : String = ""
    var Port : Int = 8888
}