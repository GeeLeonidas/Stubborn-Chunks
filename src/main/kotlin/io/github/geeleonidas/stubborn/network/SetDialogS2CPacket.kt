package io.github.geeleonidas.stubborn.network

import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.StubbornS2CPacket
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.PacketContext
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf

object SetDialogS2CPacket: StubbornS2CPacket {
    override val id = Stubborn.makeId("set_dialog")
    init { this.register() }

    override fun accept(packetContext: PacketContext, packetByteBuf: PacketByteBuf) {
        val moddedPlayer = packetContext.player as StubbornPlayer
        val bimoe = packetByteBuf.readEnumConstant(Bimoe::class.java)
        val dialogId = packetByteBuf.readString()
        packetContext.taskQueue.execute {
            moddedPlayer.setCurrentDialog(bimoe, dialogId)
        }
    }

    override fun sendToPlayer(player: PlayerEntity, bimoe: Bimoe) {
        val dialogId = (player as StubbornPlayer).getCurrentDialog(bimoe)
        val packetByteBuf = PacketByteBuf(Unpooled.buffer())
        packetByteBuf.writeEnumConstant(bimoe)
        packetByteBuf.writeString(dialogId)
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, id, packetByteBuf)
    }
}