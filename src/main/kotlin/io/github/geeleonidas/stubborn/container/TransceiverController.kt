package io.github.geeleonidas.stubborn.container

import io.github.cottonmc.cotton.gui.CottonCraftingController
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WTextField
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.block.entity.TransceiverBlockEntity
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.recipe.RecipeType
import net.minecraft.text.LiteralText
import net.minecraft.util.PacketByteBuf
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper

class TransceiverController(syncId: Int, playerEntity: PlayerEntity, pos: BlockPos):
    CottonCraftingController(RecipeType.CRAFTING, syncId, playerEntity.inventory) {
    private val ref = playerEntity.world.getBlockEntity(pos)!! as TransceiverBlockEntity
    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setSize(300, 200)

        val btnAdd = WButton(LiteralText("+"))
        val btnMns = WButton(LiteralText("-"))
        val field = WTextField()
        field.text = "${ref.chunkRadius}"

        btnAdd.setOnClick {
            sendButtonPacket(ref, true)
            field.text = "${ref.chunkRadius}"
        }
        btnMns.setOnClick {
            sendButtonPacket(ref, false)
            field.text = "${ref.chunkRadius}"
        }

        root.add(btnMns, 0, 0)
        root.add(btnAdd, 2, 0)
        root.add(field, 1, 1)

        root.validate(this)
    }
}

private fun sendButtonPacket(ref: TransceiverBlockEntity, increasing: Boolean) {
    val newValue = ref.chunkRadius.toInt() + if(increasing) 1 else -1
    ref.chunkRadius = MathHelper.clamp(newValue, 0, 3).toByte()
    ref.markDirty()

    val passedData = PacketByteBuf(Unpooled.buffer())
    passedData.writeBlockPos(ref.pos)
    passedData.writeBoolean(increasing)
    ClientSidePacketRegistry.INSTANCE.sendToServer(Stubborn.packetTransceiverButtonId, passedData)
}