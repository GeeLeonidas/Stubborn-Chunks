package io.github.geeleonidas.stubborn.network

import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.StubbornC2SPacket
import io.github.geeleonidas.stubborn.resource.DialogManager
import io.github.geeleonidas.stubborn.resource.dialog.UpdateDialog
import io.github.geeleonidas.stubborn.screen.TransceiverGuiDescription
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.fabricmc.fabric.api.network.PacketContext
import net.minecraft.network.PacketByteBuf

object UpdateProgressC2SPacket: StubbornC2SPacket {
    override val id = Stubborn.makeId("update_progress")
    init { register() }

    override fun accept(packetContext: PacketContext, packetByteBuf: PacketByteBuf) {
        val transceiverGuiDescription = packetContext.player.currentScreenHandler
        val playerEntity = packetContext.player
        val bimoe = packetByteBuf.readEnumConstant(Bimoe::class.java)
        packetContext.taskQueue.execute {
            if (!transceiverGuiDescription.canUse(playerEntity) ||
                transceiverGuiDescription !is TransceiverGuiDescription)
                return@execute

            if (transceiverGuiDescription.bimoe != bimoe)
                return@execute

            val moddedPlayer = playerEntity as StubbornPlayer
            val currentDialog = DialogManager.findDialog(bimoe,
                DialogManager.getDialog(bimoe, playerEntity).nextDialogsIds[0]
            )

            if (currentDialog !is UpdateDialog)
                return@execute

            currentDialog.executeUpdate.invoke(bimoe, moddedPlayer)
        }
    }
}