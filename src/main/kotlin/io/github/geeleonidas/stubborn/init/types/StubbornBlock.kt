package io.github.geeleonidas.stubborn.init.types

import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.init.StubbornInit
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier

interface StubbornBlock {
    val id: Identifier

    fun register(ref: Block, hasItem: Boolean = true) {
        if (hasItem)
            StubbornInit.addItem(id, BlockItem(ref, Item.Settings().group(Stubborn.modItemGroup)))

        StubbornInit.addBlock(id, ref)
    }
}