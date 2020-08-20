package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.widget.WText
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.geeleonidas.stubborn.resource.dialog.component.EntryTextEffect
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.text.LiteralText

abstract class AbstractWDialogText(
    private val onClick: () -> Unit
): WText(LiteralText("")) {
    @Environment(EnvType.CLIENT)
    var entry = ""
        set(value) {
            actualIndex = 0
            timeAnchor = 0L
            this.setText(LiteralText(""))

            if (textEffectList.isNotEmpty()) {
                val startList = mutableListOf<Int>()
                val rangeList = mutableListOf<IntRange>()

                value.forEachIndexed { charIndex, char ->
                    if (char == '[')
                        startList += charIndex
                    else if (char == ']') {
                        rangeList += startList.last() until charIndex-1
                        startList.removeAt(startList.lastIndex)
                    }
                }

                sectorList = rangeList.toList()

                field = value.filterNot { it == '[' || it == ']' }
            }
            else {
                sectorList = emptyList()

                field = value
            }
        }

    @Environment(EnvType.CLIENT)
    var textEffectList = emptyList<EntryTextEffect>()

    @Environment(EnvType.CLIENT)
    protected var sectorList = emptyList<IntRange>()

    @Environment(EnvType.CLIENT)
    protected var actualIndex = 0

    @Environment(EnvType.CLIENT)
    protected var timeAnchor = 0L

    @Environment(EnvType.CLIENT)
    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        if (button == 0) // Left click
            onClick.invoke()
        return super.onMouseUp(x, y, button)
    }
}