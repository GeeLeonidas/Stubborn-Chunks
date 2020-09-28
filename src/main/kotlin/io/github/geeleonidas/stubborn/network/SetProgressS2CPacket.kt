package io.github.geeleonidas.stubborn.network

import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.init.types.StubbornS2CPacket
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.PacketContext
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf

object SetProgressS2CPacket: StubbornS2CPacket {
    override val id = Stubborn.makeId("set_progress")

    override fun accept(packetContext: PacketContext, packetByteBuf: PacketByteBuf) {
        val moddedPlayer = packetContext.player as StubbornPlayer
        val bimoe = packetByteBuf.readEnumConstant(Bimoe::class.java)
        val progress = packetByteBuf.readInt()
        packetContext.taskQueue.execute {
            moddedPlayer.setBimoeProgress(bimoe, progress)
        }
    }

    override fun sendToPlayer(player: PlayerEntity, bimoe: Bimoe) {
        val progress = (player as StubbornPlayer).getBimoeProgress(bimoe)
        val packetByteBuf = PacketByteBuf(Unpooled.buffer())
        packetByteBuf.writeEnumConstant(bimoe)
        packetByteBuf.writeInt(progress)
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, id, packetByteBuf)
    }
}