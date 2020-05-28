package io.github.geeleonidas.stubborn.container

import io.github.cottonmc.cotton.gui.CottonCraftingController
import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WText
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.recipe.RecipeType
import net.minecraft.text.LiteralText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome

class TransceiverController(syncId: Int, playerEntity: PlayerEntity, pos: BlockPos):
    CottonCraftingController(RecipeType.CRAFTING, syncId, playerEntity.inventory) {

    private val bimoe = selectBimoe(world.getBiome(pos))

    private val dialog: WPlainPanel
    private val text: WText

    private val moddedPlayer: StubbornPlayer =
        playerEntity as StubbornPlayer

    init {
        val root = WPlainPanel()
        setRootPanel(root)
        root.setSize(400, 320)

        dialog = object: WPlainPanel() {
            override fun onClick(x: Int, y: Int, button: Int) = nextDialog()
        }
        root.add(dialog, (root.width - dialogSizeX) / 2, (root.height - dialogSizeY) / 2 )
        dialog.setSize(dialogSizeX, dialogSizeY)
        dialog.backgroundPainter = BackgroundPainter.VANILLA

        val name = WLabel(bimoe.name.toLowerCase().capitalize())
        dialog.add(name, 0, 0)

        text = WText(LiteralText("0123456789ABCDEFGHIJKLMNOPQ0123456789ABCDEFGHIJKLMNOPQ"))
        dialog.add(text, 0, 14, dialogSizeX, dialogSizeY - 14)

        root.validate(this)
    }

    fun nextDialog() {
        TODO("Implement dialog structure")
    }

    override fun addPainters() {}
}

private const val dialogSizeX = 160
private const val dialogSizeY =  70

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