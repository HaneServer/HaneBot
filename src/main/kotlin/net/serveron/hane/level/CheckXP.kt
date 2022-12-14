package net.serveron.hane.level

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.QuerySnapshot
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.serveron.hane.HaneBot
import net.dv8tion.jda.api.EmbedBuilder
import java.time.LocalDate
import java.util.*

class CheckXP: Command() {
    init {
        this.name = "level"
        this.help = "Level確認コマンドです"
    }

    override fun execute(event: CommandEvent) {
        try {
            // Discordと連動しているUserの取得
            val future: ApiFuture<QuerySnapshot> =
                HaneBot().db.collection("users").whereEqualTo("Discord", event.member.id).get()
            val documents = future.get().documents

            // documentから絞り出す
            var level: Long? = 0
            var name: String? = null
            for (document in documents) {
                level = document.getLong("Level")
                name = document.getString("Name")
            }

            // rankの設定
            val rank = checkRank(level)
            // embed作成
            val eb = EmbedBuilder()
                .setTitle(rank.rank)
                .setAuthor(name, "https://haneserver.github.io/hane-server-blog/", event.member.user.avatarUrl)
                .setThumbnail(rank.image)
                .addField("${name}のLevel", level.toString(), true)
                .setFooter(LocalDate.now().toString())
            // reply
            event.reply(eb.build())
        } catch (e: Exception) {
            // error時に失敗しました宣言
            event.member.user.openPrivateChannel().flatMap { channel -> channel.sendMessage("ユーザー情報の取得に失敗しました！いますぐこちらからログインしてください！\nhtps://hanesansaikyou.com") }.queue()
        }
    }

    // rank分け
    fun checkRank(level: Long?): RankValue {
        return when (level) {
            null -> RankValue("Levelが取得できませんでした。運営に問い合わせてください。", "https://cdn.discordapp.com/attachments/1021048950578487396/1021052902342668298/zetsubou-hiyoko.png")
            in 0L..100L -> RankValue("にわとり組", "https://cdn.discordapp.com/attachments/1021048950578487396/1021049138802073610/toriniku-chicken.png")
            in 101L..500L -> RankValue("ひよこ組", "https://cdn.discordapp.com/attachments/1021048950578487396/1021049139238285332/sumaho.png")
            in 501L..1000L -> RankValue("たまご組", "https://cdn.discordapp.com/attachments/1021048950578487396/1021049140500762674/tamagohiyokon.png")
            in 1001L..2000L -> RankValue("オムライス組(?)", "https://cdn.discordapp.com/attachments/1021048950578487396/1021049141012480050/omu-hiyoko.png")
            in 2001L..5000L -> RankValue("脳筋ゴリラ組", "https://cdn.discordapp.com/attachments/1021048950578487396/1021049141431902298/gorilla_banana.png")

            else -> RankValue("顔面蒼白。お前はもうだめ", "https://cdn.discordapp.com/attachments/1021048950578487396/1021049140085530676/humu.png")
        }
    }

    // rank の変数格納用 class
    data class RankValue(var rank: String?, var image: String?)
}