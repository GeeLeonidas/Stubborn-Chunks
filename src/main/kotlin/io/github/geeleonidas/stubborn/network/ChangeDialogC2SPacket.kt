package io.github.geeleonidas.stubborn.network

import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.StubbornC2SPacket
import io.github.geeleonidas.stubborn.container.TransceiverGuiDescription
import io.github.geeleonidas.stubborn.resource.DialogManager
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import io.netty.buffer.Unpooled
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.PacketContext
import net.minecraft.network.PacketByteBuf

object ChangeDialogC2SPacket: StubbornC2SPacket {
    override val id = Stubborn.makeId("change_dialog")
    init { register() }

    override fun accept(packetContext: PacketContext, packetBuffer: PacketByteBuf) {
        val transceiverGuiDescription = packetContext.player.currentScreenHandler
        val playerEntity = packetContext.player
        val bimoe = packetBuffer.readEnumConstant(Bimoe::class.java)
        val toDialogId = packetBuffer.readString()
        packetContext.taskQueue.execute {
            if (!transceiverGuiDescription.canUse(playerEntity) ||
                    transceiverGuiDescription !is TransceiverGuiDescription)
                return@execute

            if (transceiverGuiDescription.bimoe != bimoe)
                return@execute

            val moddedPlayer = playerEntity as StubbornPlayer
            val currentEntryIndex = moddedPlayer.getCurrentEntry(bimoe)
            val currentDialog = DialogManager.getDialog(bimoe, playerEntity)

            // Only changes the dialog on the last entry
            if (currentEntryIndex < currentDialog.entries.size - 1)
                return@execute

            // Picking a new NodeDialog resolves in this + Handling of possible anti-ghost packets
            if (toDialogId == currentDialog.id) {
                moddedPlayer.setCurrentDialog(bimoe, toDialogId)
                return@execute
            }

            // Ending a NodeDialog tree resolves in this
            if (toDialogId == "" && currentDialog.nextDialogsIds.isEmpty()) {
                moddedPlayer.setCurrentDialog(bimoe, "")
                return@execute
            }

            // Following a response pointer resolves in this
            if (currentDialog.nextDialogsIds.contains(toDialogId))
                moddedPlayer.setCurrentDialog(bimoe, toDialogId)
        }
    }

    @Environment(EnvType.CLIENT)
    fun sendToServer(bimoe: Bimoe, toDialogId: String) {
        val packetByteBuf = PacketByteBuf(Unpooled.buffer())
        packetByteBuf.writeEnumConstant(bimoe)
        packetByteBuf.writeString(toDialogId)
        ClientSidePacketRegistry.INSTANCE.sendToServer(id, packetByteBuf)
    }
}