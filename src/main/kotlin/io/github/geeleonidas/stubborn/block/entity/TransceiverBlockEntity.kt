package io.github.geeleonidas.stubborn.block.entity

import io.github.geeleonidas.stubborn.StubbornInit
import io.github.geeleonidas.stubborn.container.TransceiverGuiDescription
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.text.LiteralText

class TransceiverBlockEntity:
    BlockEntity(StubbornInit.transceiverBlockEntityType),
    NamedScreenHandlerFactory {

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity) =
        TransceiverGuiDescription(syncId, inv, pos)

    override fun getDisplayName() = LiteralText("")

}