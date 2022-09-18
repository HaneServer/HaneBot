package hane.`server-on`.hane

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
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
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun create(token: String) { // create
        val commandClient = CommandClientBuilder()
            .setPrefix(Dotenv.load().get("PREFIX")) // prefix setting
            .setOwnerId("752090084639244408") // owner id
            .addCommands(CheckXP()) // commands
            .build()

        jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS) // messageの取得を許可
            .addEventListeners(commandClient) // Regi commandClient
            .addEventListeners(AddXP()) // add jda listener
            .build()
    }

    fun jda(): JDA {
        return jda
    }
}

fun main() {

    val serviceAccount: InputStream = FileInputStream(Dotenv.load().get("FIREBASEJSON")) // .envからJSON pathを取得する
    val credentials = GoogleCredentials.fromStream(serviceAccount) // credential 設定
    val options = FirebaseOptions.builder() // option 設定
        .setCredentials(credentials)
        .build()

    FirebaseApp.initializeApp(options) // 接続

    val bot = HaneBot()
    bot.create(Dotenv.load().get("TOKEN")) // .evnからTOKEN取得
}