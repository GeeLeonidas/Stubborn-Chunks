package io.github.geeleonidas.stubborn.init.types

import io.github.geeleonidas.stubborn.init.StubbornInit
import net.minecraft.item.Item
import net.minecraft.util.Identifier

interface StubbornItem {
    val id: Identifier

    fun register(ref: Item) = StubbornInit.addItem(id, ref)
}