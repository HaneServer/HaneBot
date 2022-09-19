package net.serveron.hane.level

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.QuerySnapshot
import net.serveron.hane.HaneBot
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.*
import kotlin.concurrent.schedule


class AddXP : ListenerAdapter() {
    var limitUser = mutableListOf<Member?>() // levelアップを制限するユーザー一覧

    override fun onMessageReceived(event: MessageReceivedEvent) {
        // try {
            if (event.member?.user?.isBot == true || event.message.contentRaw.startsWith("!")) return
            if (limitUser.contains(event.member)) return // limitUserリストに入っていたら無効

            // Discordと連動しているUserの取得
            val future: ApiFuture<QuerySnapshot> =
                HaneBot().db.collection("users").whereEqualTo("Discord", event.member?.id).get()
            val documents = future.get().documents

            // levelのアップデート
            HaneBot().db.collection("users").document(documents[0].id)
                .update("Level", (documents[0].getLong("Level")?.plus(1)))
        /*} catch (e: Exception) {
            event.member?.user?.openPrivateChannel()
                ?.flatMap { channel -> channel.sendMessageEmbeds(EmbedBuilder()
                    .setTitle("ユーザー認証に失敗しました")
                    .setDescription("アカウント登録 -> https://hanesansaikyou.com\n既にアカウントがある場合 -> `!link` をここで発言してください")
                    .build()) }?.queue()
        }*/

        // 成功したらlimitUserに入れて、1分後に解除
        limitUser.add(event.member)
        Timer().schedule(60000) {
            limitUser.remove(event.member)
        }
    }
}