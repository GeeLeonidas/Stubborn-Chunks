package io.github.geeleonidas.stubborn.client

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.StubbornInit
import io.github.geeleonidas.stubborn.container.TransceiverController
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos

class TransceiverScreen(syncId: Int, playerEntity: PlayerEntity, pos: BlockPos):
    CottonInventoryScreen<TransceiverController>(TransceiverController(syncId, playerEntity, pos), playerEntity)

@Suppress("unused")
fun init() {
    ScreenProviderRegistry.INSTANCE.registerFactory(StubbornInit.transceiverBlock.id) {
            syncId, _, playerEntity, buf -> TransceiverScreen(syncId, playerEntity, buf.readBlockPos())
    }

    Stubborn.log("Biomes registered as EntityType.MOE successfully!")
}