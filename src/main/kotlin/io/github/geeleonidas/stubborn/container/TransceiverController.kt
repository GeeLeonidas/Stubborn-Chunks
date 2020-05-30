package io.github.geeleonidas.stubborn.container

import io.github.cottonmc.cotton.gui.CottonCraftingController
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.client.widget.WDialogBox
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.recipe.RecipeType
import net.minecraft.util.math.BlockPos

class TransceiverController(syncId: Int, playerEntity: PlayerEntity, pos: BlockPos):
    CottonCraftingController(RecipeType.CRAFTING, syncId, playerEntity.inventory) {

    init {
        val root = WPlainPanel()
        setRootPanel(root)
        root.setSize(400, 320)

        val bimoe = Bimoe.fromBiome(world.getBiome(pos))
        val dialog = WDialogBox(bimoe, playerEntity as StubbornPlayer)
        root.add(dialog, (root.width - dialog.width) / 2, (root.height - dialog.height) / 2)

        root.validate(this)
    }

    override fun addPainters() {}
}