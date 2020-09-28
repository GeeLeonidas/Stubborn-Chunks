package io.github.geeleonidas.stubborn.init.types

import io.github.geeleonidas.stubborn.Bimoe
import io.netty.buffer.Unpooled
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.PacketConsumer
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

interface StubbornS2CPacket: PacketConsumer {
    val id: Identifier
    fun register() =
        ClientSidePacketRegistry.INSTANCE.register(id, this)
    @Environment(EnvType.SERVER)
    fun sendToPlayer(player: PlayerEntity, bimoe: Bimoe) {
        val packetByteBuf = PacketByteBuf(Unpooled.buffer())
        packetByteBuf.writeEnumConstant(bimoe)
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, id, packetByteBuf)
    }
}