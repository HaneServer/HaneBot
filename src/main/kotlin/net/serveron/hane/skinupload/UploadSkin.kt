package net.serveron.hane.skinupload

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import io.github.cdimascio.dotenv.Dotenv
import net.dv8tion.jda.api.EmbedBuilder
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.writeText


class UploadSkin: Command() {

    init {
        this.name = "upload"
    }

    override fun execute(event: CommandEvent) {
        val args = event.args.split(" ")

        when {
            event.message.attachments.isNotEmpty() && event.args.isNotEmpty() && args.size == 2 -> {
                try {
                    val response: String? = post(args[0], event.message.attachments[0].url, args[1])

                    val jObject = JSONObject(response)
                    val data = jObject.getJSONObject("data")
                    val texture = data.getJSONObject("texture")
                    val value = texture.getString("value")
                    val signature = texture.getString("signature")

                    val file = Paths.get(Dotenv.load().get("SKINFOLDER"), "${args[0]}.skin")

                    if (!Files.exists(file)) {
                        Files.createFile(file)
                    }

                    file.writeText("${value}\n${signature}\n4102444800000", Charsets.UTF_8)

                    val eb = EmbedBuilder()
                        .setTitle("スキンの反映が完了しました！")
                        .setDescription("マイクラサーバーにログイン後、`/skin set ${args[0]}`を実行して、再ログインすると反映されます！")

                    event.reply(eb.build())
                } catch (e: IOException) {
                    event.reply("ファイルの作成に失敗しました。その名前のファイルはすでに存在するので、他の名前にしてください。")
                } catch (e: JSONException) {
                    event.reply("スキンデータの取得に失敗しました。画像サイズが64x64の場合は32x32に変更してください。")
                }

            }
            else -> {
                event.reply("名前を指定してください。`!upload <名前> <classicかslim>`\n例: `!upload masahara3 slim` \n画像を貼っていない場合は使いたいスキンの画像も載せてください。"); return
            }
        }

    }

    @Throws(IOException::class)
    fun post(name: String, image: String, variant: String): String? {

        val client: OkHttpClient = OkHttpClient.Builder().build()
        val json = """
            {
              "variant": "$variant",
              "name": "$name",
              "visibility": 0,
              "url": "$image"
            }
        """.trimIndent()

        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        val request = Request.Builder()
            .url("https://api.mineskin.org/generate/url")
            .post(body)
            .build()

        client.newCall(request).execute().use { response -> return response.body()?.string() }
    }
}