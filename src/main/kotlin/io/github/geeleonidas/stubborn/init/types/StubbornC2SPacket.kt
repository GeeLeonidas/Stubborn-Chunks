package io.github.geeleonidas.stubborn.init.types

import io.github.geeleonidas.stubborn.Bimoe
import io.netty.buffer.Unpooled
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.PacketConsumer
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

interface StubbornC2SPacket: PacketConsumer {
    val id: Identifier
    fun register() =
        ServerSidePacketRegistry.INSTANCE.register(id, this)
    @Environment(EnvType.CLIENT)
    fun sendToServer(bimoe: Bimoe) {
        val packetByteBuf = PacketByteBuf(Unpooled.buffer())
        packetByteBuf.writeEnumConstant(bimoe)
        ClientSidePacketRegistry.INSTANCE.sendToServer(id, packetByteBuf)
    }
}