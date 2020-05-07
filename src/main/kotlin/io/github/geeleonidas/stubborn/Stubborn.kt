package io.github.geeleonidas.stubborn

import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

object Stubborn {
    // Reference values
    const val modId = "stubborn"
    const val modName = "Stubborn Chunks"
    val modItemGroup: ItemGroup = ItemGroup.MISC

    private val logger = LogManager.getLogger()

    fun log(msg: String, level: Level = Level.INFO) = logger.log(level, "[$modName] $msg")
    fun makeId(id: String) = Identifier(modId, id)
}

@Suppress("unused")
fun init() {
    Stubborn.log("Optimizing biomes...")

    StubbornInit.registerBlocks()
    StubbornInit.registerItems()

    Stubborn.log("ERROR 11: [ERROR_BAD_FORMAT (0xB)]", Level.WARN)
}