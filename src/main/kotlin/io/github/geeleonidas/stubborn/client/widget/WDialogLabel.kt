package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.geeleonidas.stubborn.Bimoe
import net.minecraft.text.LiteralText

class WDialogLabel(
    bimoe: Bimoe,
    private val nextDialog: () -> Unit
): WLabel(bimoe.formattedName()) {

    private val bimoeName = bimoe.formattedName()
    var visible = true
       set(value) {
           this.setText(if (value) bimoeName else LiteralText(""))
           field = value
       }

    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        if (button == 0) // Left click
            nextDialog.invoke()
        return super.onMouseUp(x, y, button)
    }
}