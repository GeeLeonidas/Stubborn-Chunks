package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.widget.WButton
import net.minecraft.text.TranslatableText

class WResponseButton(
    private val response: TranslatableText,
    private val nextDialog: () -> Unit
): WButton(response) {
    override fun onClick(x: Int, y: Int, button: Int) {
        if (button == 0) { // Left button
            nextDialog.invoke()
        }
    }
}