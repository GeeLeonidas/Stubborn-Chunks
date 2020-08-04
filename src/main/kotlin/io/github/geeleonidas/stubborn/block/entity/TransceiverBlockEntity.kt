package io.github.geeleonidas.stubborn.block.entity

import io.github.geeleonidas.stubborn.StubbornInit
import io.github.geeleonidas.stubborn.screen.TransceiverGuiDescription
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText

class TransceiverBlockEntity:
    BlockEntity(StubbornInit.transceiverBlockEntityType),
    ExtendedScreenHandlerFactory {

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity) =
        TransceiverGuiDescription(syncId, inv, pos)

    override fun writeScreenOpeningData(serverPlayerEntity: ServerPlayerEntity?, buffer: PacketByteBuf?) {
        buffer?.writeBlockPos(pos)
    }

    override fun getDisplayName() = LiteralText("")

}