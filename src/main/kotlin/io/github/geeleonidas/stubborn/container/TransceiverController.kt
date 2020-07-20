package io.github.geeleonidas.stubborn.container

import io.github.cottonmc.cotton.gui.CottonCraftingController
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.client.widget.WBimoeSprite
import io.github.geeleonidas.stubborn.client.widget.WDialogBox
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.recipe.RecipeType
import net.minecraft.util.math.BlockPos

class TransceiverController(
    syncId: Int,
    playerEntity: PlayerEntity,
    pos: BlockPos
): CottonCraftingController(RecipeType.CRAFTING, syncId, playerEntity.inventory) {

    init {
        val root = WPlainPanel()
        setRootPanel(root)
        root.setSize(400, 320)

        val bimoe = Bimoe.fromBiome(world.getBiome(pos))

        val offsetY = 35

        val sprite = WBimoeSprite(bimoe)
        root.add(sprite, (root.width - sprite.width) / 2, root.height / 2 - sprite.height + offsetY)

        val dialog = WDialogBox(bimoe, playerEntity)
        root.add(dialog, (root.width - dialog.width) / 2, root.height / 2 + offsetY)

        root.validate(this)
    }

    override fun addPainters() {}
}