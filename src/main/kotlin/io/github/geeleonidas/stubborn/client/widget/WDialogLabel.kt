package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.Bimoe

class WDialogLabel(bimoe: Bimoe, private val nextDialog: () -> Unit):
    WLabel(Bimoe.formattedText(bimoe.name.toLowerCase().capitalize(), bimoe)) {

    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        if (button == 0) // Left click
            nextDialog.invoke()
        return super.onMouseUp(x, y, button)
    }
}