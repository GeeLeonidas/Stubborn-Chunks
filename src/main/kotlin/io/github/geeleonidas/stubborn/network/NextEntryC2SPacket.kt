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


object NextEntryC2SPacket: StubbornC2SPacket {
    override val id = Stubborn.makeId("next_entry")
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
            val currentIndex = moddedPlayer.getCurrentEntry(bimoe)
            val currentDialog = DialogManager.getDialog(bimoe, playerEntity)

            if (currentIndex < currentDialog.entries.size - 1)
                moddedPlayer.setCurrentEntry(bimoe, currentIndex + 1)
        }
    }

    @Environment(EnvType.CLIENT)
    override fun sendToServer(bimoe: Bimoe) {
        val packetByteBuf = PacketByteBuf(Unpooled.buffer())
        packetByteBuf.writeEnumConstant(bimoe)
        ClientSidePacketRegistry.INSTANCE.sendToServer(id, packetByteBuf)
    }
}