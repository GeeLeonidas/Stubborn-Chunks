package io.github.geeleonidas.stubborn.client.widget

import io.github.geeleonidas.stubborn.Bimoe
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.LiteralText

class WDialogText(
    bimoe: Bimoe,
    onClick: () -> Unit
): AbstractWDialogText(onClick) {

    @Environment(EnvType.CLIENT)
    var isTypingSoundMuted = false

    @Environment(EnvType.CLIENT)
    private val bimoeTypingSound = bimoe.typingSound

    @Environment(EnvType.CLIENT)
    private val bimoeTypingDelay = bimoe.typingDelay

    @Environment(EnvType.CLIENT)
    private var typingFactor = 1f

    @Environment(EnvType.CLIENT)
    private fun playTypingSound() =
        MinecraftClient.getInstance().player?.playSound(
            bimoeTypingSound, SoundCategory.VOICE, 0.8f, 1f
        )

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

            if (!isTypingSoundMuted && actualLetter != ' ')
                playTypingSound()

            typingFactor = 1f
            sectorList.forEachIndexed { sectorIndex, sector ->
                if (sector.contains(actualIndex + 1)) {
                    val effect = textEffectList[sectorIndex]
                    typingFactor *= effect.typingModifier
                }
            }
        }
    }
}
