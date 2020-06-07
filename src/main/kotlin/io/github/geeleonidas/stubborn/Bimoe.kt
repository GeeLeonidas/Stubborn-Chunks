package io.github.geeleonidas.stubborn

import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome

enum class Bimoe(private val formatting: Formatting) {
    SILVIS(Formatting.DARK_GREEN), FINIS(Formatting.BLACK), SORBIRE(Formatting.DARK_PURPLE),
    ERIMOS(Formatting.GOLD), LAVINA(Formatting.DARK_BLUE), MANAMI(Formatting.DARK_AQUA);

    fun lowerCasedName() =
        this.name.toLowerCase()
    fun capitalizedName() =
        lowerCasedName().capitalize()
    fun formattedName(): Text =
        LiteralText(capitalizedName()).formatted(this.formatting)

    companion object {
        fun fromBiome(biome: Biome): Bimoe {
            if (biome.precipitation == Biome.Precipitation.SNOW)
                return LAVINA

            return when (biome.category) {
                Biome.Category.DESERT, Biome.Category.MESA, Biome.Category.SAVANNA -> ERIMOS

                Biome.Category.OCEAN, Biome.Category.BEACH, Biome.Category.RIVER -> MANAMI

                Biome.Category.SWAMP, Biome.Category.MUSHROOM, Biome.Category.NETHER -> SORBIRE

                Biome.Category.THEEND, Biome.Category.NONE -> FINIS

                else -> SILVIS
            }
        }
    }
}