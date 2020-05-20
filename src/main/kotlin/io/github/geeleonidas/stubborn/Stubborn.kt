package io.github.geeleonidas.stubborn

import io.github.geeleonidas.stubborn.container.TransceiverController
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

object Stubborn {
    // Reference values
    const val modId = "stubborn"
    const val modName = "Stubborn Chunks"
    val modItemGroup: ItemGroup = ItemGroup.MISC

    enum class Bimoe {
        NIHILLY, AYUMI, SILVIS, ERIMOS, LAVINA
    }

    private val logger = LogManager.getLogger()

    fun log(msg: String, level: Level = Level.INFO) = logger.log(level, "[$modName] $msg")
    fun makeId(id: String) = Identifier(modId, id)
}

@Suppress("unused")
fun init() {
    Stubborn.log("Optimizing biomes...")

    StubbornInit.registerBlocks()
    StubbornInit.registerItems()

    ContainerProviderRegistry.INSTANCE.registerFactory(StubbornInit.transceiverBlock.id) {
        syncId, _, playerEntity, buf -> TransceiverController(syncId, playerEntity, buf.readBlockPos())
    }

    Stubborn.log("ERROR 11: [ERROR_BAD_FORMAT (0xB)]", Level.WARN)
}