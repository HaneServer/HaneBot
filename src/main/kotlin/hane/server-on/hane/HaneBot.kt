package hane.`server-on`.hane

import com.jagrosh.jdautilities.command.CommandClientBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent

class HaneBot {
    private lateinit var jda: JDA

    fun create(token: String) {
        val commandClient = CommandClientBuilder()
            .setPrefix("h.")
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
    bot.create("MTAyMDE5NTAxNTM0NTA1Nzg1NQ.Giy8ro.8LthzkyU6TSkqBvcgzvhaknnpq7LOLyEE2f1bo")
}