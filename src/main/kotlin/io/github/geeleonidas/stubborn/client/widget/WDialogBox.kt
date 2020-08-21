package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.geeleonidas.stubborn.Bimoe
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

class WDialogBox(
    bimoe: Bimoe
): WPlainPanel() {

    val dialogLabel: WDialogLabel
    val dialogText: WDialogText

    @Environment(EnvType.CLIENT)
    private var callNextEntry = {}

    init {
        val dialogSizeX = 180
        val dialogSizeY = 50
        val textOffsetY = 14

        setSize(dialogSizeX, dialogSizeY)
        backgroundPainter = BackgroundPainter.VANILLA

        dialogLabel = WDialogLabel(bimoe, this::onClick)
        add(dialogLabel, 0, 0)

        dialogText = WDialogText(bimoe, this::onClick)
        add(dialogText, 0, textOffsetY, dialogSizeX, dialogSizeY - textOffsetY)
    }

    @Environment(EnvType.CLIENT)
    fun setCallNextEntry(nextEntryLambda: () -> Unit) =
        this::callNextEntry.set(nextEntryLambda)

    @Environment(EnvType.CLIENT)
    private fun onClick() {
        if (dialogText.isFinished())
            callNextEntry.invoke()
        else
            dialogText.finish()
    }

    @Environment(EnvType.CLIENT)
    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        if (button == 0)
            onClick()
        return super.onMouseUp(x, y, button)
    }
}