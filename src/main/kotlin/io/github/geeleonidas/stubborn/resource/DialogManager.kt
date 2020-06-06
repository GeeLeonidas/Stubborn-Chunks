package io.github.geeleonidas.stubborn.resource

import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity

object DialogManager {
    fun getDialog(bimoe: Bimoe, playerEntity: PlayerEntity) {
        val moddedPlayer = playerEntity as StubbornPlayer
        // moddedPlayer.getCurrentDialog()
        // if null, return pickNewDialog()
        // if not null, just return it
    }

    private fun pickNewDialog(bimoe: Bimoe, playerEntity: PlayerEntity) {
        // Picks a new random dialog based on DialogCondition and Bimoe progress
    }
}