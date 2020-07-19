package io.github.geeleonidas.stubborn

import io.github.geeleonidas.stubborn.container.TransceiverController
import io.github.geeleonidas.stubborn.resource.DialogManager
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.item.ItemGroup
import net.minecraft.resource.NamespaceResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

object Stubborn {
    const val modId = "stubborn"
    const val modName = "Stubborn"

    val modItemGroup: ItemGroup = ItemGroup.MISC
    private val logger = LogManager.getLogger()

    fun log(msg: String, level: Level = Level.INFO) = logger.log(level, "[$modName] $msg")
    fun makeId(id: String) = Identifier(modId, id)
    fun resource(path: String) = "/assets/$modId/$path"
}

@Suppress("unused")
fun init() {
    Stubborn.log("Optimizing biomes...")

    StubbornInit.registerBlocks()
    StubbornInit.registerItems()

    ContainerProviderRegistry.INSTANCE.registerFactory(StubbornInit.transceiverBlock.id) {
            syncId, _, playerEntity, buf -> TransceiverController(syncId, playerEntity, buf.readBlockPos())
    }

    DialogManager.initialize()
}