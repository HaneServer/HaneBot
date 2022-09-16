package hane.`server-on`.hane

import com.google.gson.Gson
import com.jagrosh.jdautilities.command.CommandClientBuilder
import io.github.cdimascio.dotenv.Dotenv
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import java.io.File
import java.io.InputStreamReader
import java.io.ObjectInputFilter

class HaneBot {
    private lateinit var jda: JDA

    val env = Dotenv.configure().load()
    fun create(token: String) {
        val commandClient = CommandClientBuilder()
            .setPrefix(env.get("PREFIX"))
            .setOwnerId("752090084639244408")
            .addCommands()
            .build()

        jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES)
            .addEventListeners(commandClient)
            .build()
    }

    fun jda(): JDA {
        return jda
    }
}

fun main() {
    val bot = HaneBot()
    val env = Dotenv.load()
    bot.create(env.get("TOKEN"))
}