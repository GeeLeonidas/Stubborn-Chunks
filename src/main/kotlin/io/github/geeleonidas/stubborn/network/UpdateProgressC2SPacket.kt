package io.github.geeleonidas.stubborn.network

import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.StubbornC2SPacket
import io.github.geeleonidas.stubborn.resource.DialogManager
import io.github.geeleonidas.stubborn.screen.TransceiverGuiDescription
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import io.netty.buffer.Unpooled
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
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
            val currentDialog = DialogManager.getDialog(bimoe, playerEntity)

            if (currentDialog.nextDialogsIds.size != 1)
                return@execute

            val isForward = currentDialog.nextDialogsIds[0] == "~progress_forward"
            val isBackward = currentDialog.nextDialogsIds[0] == "~progress_backward"

            if (!(isForward || isBackward))
                return@execute

            moddedPlayer.setCurrentDialog(bimoe, "")

            val delta = if (isForward) +1 else -1
            moddedPlayer.updateBimoeProgress(bimoe, delta)
        }
    }

    @Environment(EnvType.CLIENT)
    fun sendToServer(bimoe: Bimoe) {
        val packetByteBuf = PacketByteBuf(Unpooled.buffer())
        packetByteBuf.writeEnumConstant(bimoe)
        ClientSidePacketRegistry.INSTANCE.sendToServer(id, packetByteBuf)
    }
}