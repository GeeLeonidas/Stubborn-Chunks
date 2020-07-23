package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.geeleonidas.stubborn.Stubborn
import net.minecraft.text.TranslatableText

class WResponseButton(
    private val response: TranslatableText,
    private val toDialogId: String,
    private val nextDialog: (String) -> Unit
): WButton(response) {
    override fun onClick(x: Int, y: Int, button: Int) {
        if (button == 0) { // Left button
            Stubborn.log(response.asString())
            nextDialog.invoke(toDialogId)
        }
    }
}