package io.github.geeleonidas.stubborn

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
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
import io.netty.buffer.Unpooled
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.PacketConsumer
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.command.arguments.ArgumentTypes
import net.minecraft.command.arguments.serialize.ConstantArgumentSerializer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
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
        ChangeDialogC2SPacket.initialize()
        NextEntryC2SPacket.initialize()
    }

    @Environment(EnvType.CLIENT)
    fun registerS2CPackets() {
        SetDialogS2CPacket.initialize()
        SetProgressCommand.initialize()
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
        SetDialogCommand.initialize()
        SetProgressCommand.initialize()
    }
}

interface StubbornBlock {
    val id: Identifier

    fun register(ref: Block, hasItem: Boolean = true) {
        if (hasItem)
            StubbornInit.addItem(id, BlockItem(ref, Item.Settings().group(Stubborn.modItemGroup)))

        StubbornInit.addBlock(id, ref)
    }
}

interface StubbornItem {
    val id: Identifier

    fun register(ref: Item) = StubbornInit.addItem(id, ref)
}

interface StubbornC2SPacket: PacketConsumer {
    val id: Identifier
    fun initialize() = Unit
    fun register() =
        ServerSidePacketRegistry.INSTANCE.register(id, this)
    @Environment(EnvType.CLIENT)
    fun sendToServer(bimoe: Bimoe) {
        val packetByteBuf = PacketByteBuf(Unpooled.buffer())
        packetByteBuf.writeEnumConstant(bimoe)
        ClientSidePacketRegistry.INSTANCE.sendToServer(id, packetByteBuf)
    }
}

interface StubbornS2CPacket: PacketConsumer {
    val id: Identifier
    fun initialize() = Unit
    fun register() =
        ClientSidePacketRegistry.INSTANCE.register(id, this)
    @Environment(EnvType.SERVER)
    fun sendToPlayer(player: PlayerEntity, bimoe: Bimoe) {
        val packetByteBuf = PacketByteBuf(Unpooled.buffer())
        packetByteBuf.writeEnumConstant(bimoe)
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, id, packetByteBuf)
    }
}

interface StubbornServerCommand: Command<ServerCommandSource> {
    val literalBuilder: LiteralArgumentBuilder<ServerCommandSource>
    fun initialize() = Unit
    fun register(onlyOnDedicated: Boolean = false) {
        CommandRegistrationCallback.EVENT.register(
            CommandRegistrationCallback { dispatcher, isDedicated ->
                if (isDedicated || !onlyOnDedicated)
                    dispatcher.register(
                        CommandManager
                            .literal(Stubborn.modId)
                            .then(literalBuilder)
                    )
            }
        )
    }
}