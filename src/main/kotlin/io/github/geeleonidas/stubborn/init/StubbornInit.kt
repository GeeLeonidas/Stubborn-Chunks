package io.github.geeleonidas.stubborn.init

import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.block.TransceiverBlock
import io.github.geeleonidas.stubborn.block.entity.TransceiverBlockEntity
import io.github.geeleonidas.stubborn.network.ChangeDialogC2SPacket
import io.github.geeleonidas.stubborn.network.NextEntryC2SPacket
import io.github.geeleonidas.stubborn.network.SetDialogS2CPacket
import io.github.geeleonidas.stubborn.screen.TransceiverGuiDescription
import io.github.geeleonidas.stubborn.server.command.SetDialogCommand
import io.github.geeleonidas.stubborn.server.command.SetProgressCommand
import io.github.geeleonidas.stubborn.server.command.arguments.BimoeArgumentType
import io.github.geeleonidas.stubborn.server.command.arguments.DialogIdArgumentType
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.command.arguments.ArgumentTypes
import net.minecraft.command.arguments.serialize.ConstantArgumentSerializer
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.function.Supplier

object StubbornInit {

    private val registeredBlocks = mutableMapOf<Identifier, Block>()
    private val registeredItems = mutableMapOf<Identifier, Item>()

    val transceiverBlock = TransceiverBlock()
    val transceiverBlockEntityType = Registry.register(
        Registry.BLOCK_ENTITY_TYPE, transceiverBlock.id,
        BlockEntityType.Builder.create(Supplier { TransceiverBlockEntity() }, transceiverBlock).build(null)
    )
    val transceiverHandlerType =
        ScreenHandlerRegistry.registerExtended<TransceiverGuiDescription>(transceiverBlock.id) {
                syncId, playerInventory, buffer ->
            TransceiverGuiDescription(syncId, playerInventory, buffer.readBlockPos())
    }

    fun addBlock(id: Identifier, block: Block) = registeredBlocks.putIfAbsent(id, block)
    fun addItem(id: Identifier, item: Item) = registeredItems.putIfAbsent(id, item)

    fun registerBlocks() {
        for (pair in registeredBlocks)
            Registry.register(Registry.BLOCK, pair.key, pair.value)
    }

    fun registerItems() {
        for (pair in registeredItems)
            Registry.register(Registry.ITEM, pair.key, pair.value)
    }

    fun registerC2SPackets() {
        ChangeDialogC2SPacket.register()
        NextEntryC2SPacket.register()
    }

    @Environment(EnvType.CLIENT)
    fun registerS2CPackets() {
        SetDialogS2CPacket.register()
        SetProgressCommand.register()
    }

    fun registerArgumentTypes() {
        ArgumentTypes.register(
            "${Stubborn.modId}:bimoe",
            BimoeArgumentType::class.java,
            ConstantArgumentSerializer { BimoeArgumentType }
        )
        ArgumentTypes.register(
            "${Stubborn.modId}:dialog_id",
            DialogIdArgumentType::class.java,
            ConstantArgumentSerializer { DialogIdArgumentType }
        )
    }

    fun registerServerCommands() {
        SetDialogCommand.register()
        SetProgressCommand.register()
    }
}