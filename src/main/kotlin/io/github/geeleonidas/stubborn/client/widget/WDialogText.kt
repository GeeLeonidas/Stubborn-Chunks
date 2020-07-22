package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.widget.WText
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.text.LiteralText

class WDialogText(
    private val nextDialog: () -> Unit
): WText(LiteralText("")) {

    var entry = ""
        set(value) {
            actualIndex = 0
            timeAnchor = System.currentTimeMillis()
            this.setText(LiteralText(""))
            field = value
        }
    private var actualIndex = 0
    private var timeAnchor = System.currentTimeMillis()

    fun finish() {
        this.setText(LiteralText(entry))
        actualIndex = entry.length
    }
    fun isFinished() = (actualIndex >= entry.length)

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

    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        if (button == 0) // Left click
            nextDialog.invoke()
        return super.onMouseUp(x, y, button)
    }
}
