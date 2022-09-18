package hane.`server-on`.hane

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import com.jagrosh.jdautilities.command.CommandClientBuilder
import hane.`server-on`.hane.level.AddXP
import hane.`server-on`.hane.level.CheckXP
import io.github.cdimascio.dotenv.Dotenv
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import java.io.FileInputStream

import java.io.InputStream

class HaneBot {
    private lateinit var jda: JDA
    val db: Firestore = FirestoreClient.getFirestore()

    fun create(token: String) {
        val commandClient = CommandClientBuilder()
            .setPrefix(Dotenv.load().get("PREFIX"))
            .setOwnerId("752090084639244408")
            .addCommands(CheckXP())
            .build()

        jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES)
            .addEventListeners(commandClient)
            .addEventListeners(AddXP())
            .build()
    }

    fun jda(): JDA {
        return jda
    }
}

fun main() {

    val serviceAccount: InputStream = FileInputStream(Dotenv.load().get("FIREBASEJSON"))
    val credentials = GoogleCredentials.fromStream(serviceAccount)
    val options = FirebaseOptions.builder()
        .setCredentials(credentials)
        .build()

    FirebaseApp.initializeApp(options)

    val bot = HaneBot()
    bot.create(Dotenv.load().get("TOKEN")) // .evnからTOKEN取得
}