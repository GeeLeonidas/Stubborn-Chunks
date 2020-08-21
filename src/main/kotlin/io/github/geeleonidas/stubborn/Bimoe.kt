package io.github.geeleonidas.stubborn

import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.biome.Biome

enum class Bimoe(
    private val formatting: Formatting,
    val typingDelay: Int = 40
) {

    SILVIS(Formatting.DARK_GREEN),
    FINIS(Formatting.BLACK, 45),
    SORBIRE(Formatting.DARK_PURPLE, 35),

    ERIMOS(Formatting.GOLD,30),
    LAVINA(Formatting.DARK_BLUE, 50),
    MANAMI(Formatting.DARK_AQUA)

    ;

    val lowerCasedName =
        this.name.toLowerCase()
    val capitalizedName =
        lowerCasedName.capitalize()
    val formattedName: Text =
        LiteralText(capitalizedName).formatted(this.formatting)
    fun makeCompoundTagKey(key: String) =
        "${this.name}_${key.toUpperCase()}"

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