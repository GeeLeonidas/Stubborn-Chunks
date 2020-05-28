package io.github.geeleonidas.stubborn.container

import io.github.cottonmc.cotton.gui.CottonCraftingController
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.client.widget.WDialogBox
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.recipe.RecipeType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome

class TransceiverController(syncId: Int, playerEntity: PlayerEntity, pos: BlockPos):
    CottonCraftingController(RecipeType.CRAFTING, syncId, playerEntity.inventory) {

    init {
        val root = WPlainPanel()
        setRootPanel(root)
        root.setSize(400, 320)

        val dialog = WDialogBox(selectBimoe(world.getBiome(pos)), playerEntity as StubbornPlayer)
        root.add(dialog, (root.width - dialog.width) / 2, (root.height - dialog.height) / 2)

        root.validate(this)
    }

    override fun addPainters() {}
}

// TODO: Gather all util functions into one single object
private fun selectBimoe(biome: Biome): Stubborn.Bimoe {
    if (biome.precipitation == Biome.Precipitation.SNOW)
        return Stubborn.Bimoe.LAVINA

    return when(biome.category) {
        Biome.Category.DESERT, Biome.Category.MESA, Biome.Category.SAVANNA ->
            Stubborn.Bimoe.ERIMOS

        Biome.Category.OCEAN, Biome.Category.BEACH, Biome.Category.RIVER ->
            Stubborn.Bimoe.MANAMI

        Biome.Category.SWAMP, Biome.Category.MUSHROOM, Biome.Category.NETHER ->
            Stubborn.Bimoe.SORBIRE

        Biome.Category.THEEND ->
            Stubborn.Bimoe.FINIS

        else -> Stubborn.Bimoe.SILVIS
    }
}