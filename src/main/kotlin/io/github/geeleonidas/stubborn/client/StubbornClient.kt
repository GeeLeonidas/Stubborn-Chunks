package io.github.geeleonidas.stubborn.client

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.StubbornInit
import io.github.geeleonidas.stubborn.screen.TransceiverGuiDescription
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory


class TransceiverBlockScreen(gui: TransceiverGuiDescription, playerEntity: PlayerEntity):
    CottonInventoryScreen<TransceiverGuiDescription>(gui, playerEntity)

@Suppress("unused")
fun init() {
    ScreenRegistry.register<TransceiverGuiDescription, TransceiverBlockScreen>(
        StubbornInit.transceiverHandlerType
    ) { gui: TransceiverGuiDescription, inventory: PlayerInventory, _ ->
        TransceiverBlockScreen(
            gui,
            inventory.player
        )
    }

    StubbornInit.registerS2CPackets()

    Stubborn.log("Biomes registered as EntityType.MOE successfully!")
}