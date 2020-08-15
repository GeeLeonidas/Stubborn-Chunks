package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.widget.WText
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.geeleonidas.stubborn.resource.dialog.component.EntryTextEffect
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.text.LiteralText

class WDialogText(
    private val onClick: () -> Unit
): WText(LiteralText("")) {

    @Environment(EnvType.CLIENT)
    var entry = ""
        set(value) {
            actualIndex = 0
            timeAnchor = System.currentTimeMillis()
            this.setText(LiteralText(""))

            if (textEffectList.isNotEmpty()) {
                val startList = mutableListOf<Int>()
                val rangeList = mutableListOf<IntRange>()

                value.forEachIndexed { index, c ->
                    if (c == '[')
                        startList += index
                    else if (c == ']') {
                        rangeList += startList.last() until index-1
                        startList.removeAt(startList.lastIndex)
                    }
                }

                sectorList = rangeList.toList()
            }
            else
                sectorList = emptyList()

            field = value.filterNot { it == '[' || it == ']' }
        }

    @Environment(EnvType.CLIENT)
    var textEffectList = emptyList<EntryTextEffect>()

    @Environment(EnvType.CLIENT)
    private var sectorList = emptyList<IntRange>()

    @Environment(EnvType.CLIENT)
    private var actualIndex = 0

    @Environment(EnvType.CLIENT)
    private var timeAnchor = System.currentTimeMillis()

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

        if ((currentTime - timeAnchor) > 10) {
            timeAnchor = currentTime

            val actualLetter = entry[actualIndex]
            actualIndex++

            var newString = this.text.string + actualLetter
            if (actualLetter == '§') {
                newString += entry[actualIndex]
                actualIndex++
            }

            this.setText(LiteralText(newString))
        }

        super.tick()
    }

    @Environment(EnvType.CLIENT)
    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        if (button == 0) // Left click
            onClick.invoke()
        return super.onMouseUp(x, y, button)
    }
}
