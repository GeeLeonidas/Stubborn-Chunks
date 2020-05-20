package io.github.geeleonidas.stubborn.container

import io.github.cottonmc.cotton.gui.CottonCraftingController
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.geeleonidas.stubborn.Stubborn
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.recipe.RecipeType
import net.minecraft.text.LiteralText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome

class TransceiverController(syncId: Int, playerEntity: PlayerEntity, pos: BlockPos):
    CottonCraftingController(RecipeType.CRAFTING, syncId, playerEntity.inventory) {

    val bimoe = selectBimoe(world.getBiome(pos))
    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setSize(300, 200)

        root.add(WButton(LiteralText("Button")), 0, 0)

        root.validate(this)
    }
}

private fun selectBimoe(biome: Biome): Stubborn.Bimoe {
    if (biome.precipitation == Biome.Precipitation.SNOW)
        return Stubborn.Bimoe.LAVINA

    return when(biome.category) {
        Biome.Category.DESERT, Biome.Category.SAVANNA, Biome.Category.MESA ->
            Stubborn.Bimoe.ERIMOS

        Biome.Category.FOREST, Biome.Category.TAIGA, Biome.Category.JUNGLE ->
            Stubborn.Bimoe.SILVIS

        Biome.Category.OCEAN, Biome.Category.BEACH, Biome.Category.RIVER ->
            Stubborn.Bimoe.AYUMI

        else -> Stubborn.Bimoe.NIHILLY
    }
}