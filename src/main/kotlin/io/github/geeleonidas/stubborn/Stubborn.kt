package io.github.geeleonidas.stubborn

import io.github.geeleonidas.stubborn.block.entity.TransceiverBlockEntity
import io.github.geeleonidas.stubborn.container.TransceiverController
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

object Stubborn {
    // Reference values
    const val modId = "stubborn"
    const val modName = "Stubborn Chunks"
    val modItemGroup: ItemGroup = ItemGroup.MISC
    val packetTransceiverButtonId = makeId("transceiver_button")

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

    ServerSidePacketRegistry.INSTANCE.register(Stubborn.packetTransceiverButtonId) {
        packetCtx, attachedData -> run {
            val pos = attachedData.readBlockPos()
            val increasing = attachedData.readBoolean()
            packetCtx.taskQueue.execute { packetTransceiverButton(pos, increasing, packetCtx.player) }
        }
    }

    Stubborn.log("ERROR 11: [ERROR_BAD_FORMAT (0xB)]", Level.WARN)
}

private fun packetTransceiverButton(pos: BlockPos, increasing: Boolean, playerEntity: PlayerEntity) {
    val be = playerEntity.world.getBlockEntity(pos)

    if (be is TransceiverBlockEntity && playerEntity.container is TransceiverController) {
        val newValue = be.chunkRadius.toInt() + if(increasing) 1 else -1
        be.chunkRadius = MathHelper.clamp(newValue, 0, 3).toByte()
        be.markDirty()
    }
}