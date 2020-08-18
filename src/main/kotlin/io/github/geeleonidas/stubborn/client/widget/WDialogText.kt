package io.github.geeleonidas.stubborn.client.widget

import io.github.geeleonidas.stubborn.resource.dialog.component.EntryTextEffect
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.text.LiteralText

class WDialogText(
    onClick: () -> Unit
): AbstractWDialogText(onClick) {

    @Environment(EnvType.CLIENT)
    private var typingDelay = 0

    @Environment(EnvType.CLIENT)
    fun finish() {
        this.setText(LiteralText(entry))
        actualIndex = entry.length
    }

    @Environment(EnvType.CLIENT)
    fun isFinished() = (actualIndex >= entry.length)

    @Environment(EnvType.CLIENT)
    override fun tick() {
        if (isFinished())
            return

        val currentTime = System.currentTimeMillis()

        if ((currentTime - timeAnchor) > 20 + typingDelay) {
            timeAnchor = currentTime

            val actualLetter = entry[actualIndex]
            actualIndex++

            var newString = this.text.string + actualLetter
            if (actualLetter == '§') {
                newString += entry[actualIndex]
                actualIndex++
            }

            this.setText(LiteralText(newString))

            typingDelay = 0
            sectorList.forEachIndexed { sectorIndex, sector ->
                if (sector.contains(actualIndex + 1)) {
                    typingDelay += when (textEffectList[sectorIndex]) {
                        EntryTextEffect.SLOW_WRITING -> 50
                        EntryTextEffect.SPELLING -> 100
                    }
                }
            }
        }

        super.tick()
    }
}
