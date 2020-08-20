package io.github.geeleonidas.stubborn.client.widget

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText

class WDialogText(
    onClick: () -> Unit
): AbstractWDialogText(onClick) {

    @Environment(EnvType.CLIENT)
    private val bimoeTypingDelay = 40

    @Environment(EnvType.CLIENT)
    private var typingFactor = 1f

    @Environment(EnvType.CLIENT)
    fun finish() {
        this.setText(LiteralText(entry))
        actualIndex = entry.length
    }

    @Environment(EnvType.CLIENT)
    fun isFinished() = (actualIndex >= entry.length)

    @Environment(EnvType.CLIENT)
    override fun paint(matrices: MatrixStack?, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        super.paint(matrices, x, y, mouseX, mouseY)

        if (isFinished())
            return

        val currentTime = System.currentTimeMillis()

        if ((currentTime - timeAnchor) > bimoeTypingDelay / typingFactor) {
            timeAnchor = currentTime

            val actualLetter = entry[actualIndex]
            actualIndex++

            var newString = this.text.string + actualLetter
            if (actualLetter == '§') {
                newString += entry[actualIndex]
                actualIndex++
            }

            this.setText(LiteralText(newString))

            typingFactor = 1f
            sectorList.forEachIndexed { sectorIndex, sector ->
                if (sector.contains(actualIndex + 1))
                    typingFactor *= textEffectList[sectorIndex].typingModifier
            }
        }
    }
}
