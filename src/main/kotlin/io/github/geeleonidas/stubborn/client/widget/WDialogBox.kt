package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.geeleonidas.stubborn.Bimoe

class WDialogBox(
    bimoe: Bimoe,
    currentEntry: String,
    private val callNextEntry: ((String) -> Unit) -> Unit
): WPlainPanel() {

    private val dialogLabel: WDialogLabel
    private val dialogText: WDialogText

    init {
        val dialogSizeX = 180
        val dialogSizeY = 50
        val textOffsetY = 14

        setSize(dialogSizeX, dialogSizeY)
        backgroundPainter = BackgroundPainter.VANILLA

        dialogLabel = WDialogLabel(bimoe, this::onClick)
        add(dialogLabel, 0, 0)

        dialogText = WDialogText(this::onClick)
        add(dialogText, 0, textOffsetY, dialogSizeX, dialogSizeY - textOffsetY)

        dialogText.entry = currentEntry
    }

    private fun onClick() {
        if (dialogText.isFinished())
            callNextEntry.invoke(dialogText::entry::set)
        else
            dialogText.finish()
    }

    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        if (button == 0)
            onClick()
        return super.onMouseUp(x, y, button)
    }
}