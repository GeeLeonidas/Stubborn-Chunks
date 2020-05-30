package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.widget.WText
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class WDialogText(text: String, private val nextDialog: () -> Unit):
    WText(LiteralText(text)) {

    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        if (button == 0) // Left click
            nextDialog.invoke()
        return super.onMouseUp(x, y, button)
    }
}