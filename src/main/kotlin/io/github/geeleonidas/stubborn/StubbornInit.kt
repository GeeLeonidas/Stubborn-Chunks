package io.github.geeleonidas.stubborn

import io.github.geeleonidas.stubborn.block.TransceiverBlock
import io.github.geeleonidas.stubborn.block.entity.TransceiverBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.function.Supplier

object StubbornInit {
    val registeredBlocks = mutableMapOf<Identifier, Block>()
    fun registerBlocks() {
        for (pair in registeredBlocks) {
            Registry.register(Registry.BLOCK, pair.key, pair.value)
        }
    }

    val registeredItems = mutableMapOf<Identifier, Item>()
    fun registerItems() {
        for (pair in registeredItems) {
            Registry.register(Registry.ITEM, pair.key, pair.value)
        }
    }

    val transceiverBlock = TransceiverBlock()
    val transceiverBlockEntityType: BlockEntityType<TransceiverBlockEntity> = Registry.register(Registry.BLOCK_ENTITY_TYPE, transceiverBlock.id,
        BlockEntityType.Builder.create(Supplier { TransceiverBlockEntity() }, transceiverBlock).build(null))

}

interface StubbornBlock {
    val id: Identifier

    fun register(ref: Block, hasItem: Boolean = true) {
        if (hasItem)
            StubbornInit.registeredItems[id] = BlockItem(ref, Item.Settings().group(Stubborn.modItemGroup))

        StubbornInit.registeredBlocks[id] = ref
    }
}

interface StubbornItem {
    val id: Identifier

    fun register(ref: Item) {
        StubbornInit.registeredItems[id] = ref
    }
}