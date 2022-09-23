package net.serveron.hane.etc

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.requests.RestAction

class Invite : Command() {
    override fun execute(event: CommandEvent) {
        val channel = event.member.voiceState!!.channel
        channel!!.createInvite().setTargetApplication("880218394199220334")
            .flatMap { invite: Invite -> event.channel.sendMessage(invite.url) }
            .queue()
    }

    init {
        name = "invite"
        aliases = arrayOf("i")
        help = "Inviteコマンドです。`saba.invite` ポーカーができます"
        this.category = Category("music")
    }
}