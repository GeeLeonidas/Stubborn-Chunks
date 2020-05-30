package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WText
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.text.LiteralText
import java.time.Clock

class WDialogBox(
    private val bimoe: Bimoe,
    private val moddedPlayer: StubbornPlayer
): WPlainPanel() {

    private var actualEntry = ""
    private var lastTime = Clock.systemUTC().millis()
    private val dialogText: WText

    init {
        val dialogSizeX = 160
        val dialogSizeY = 50
        val textOffsetY = 14
        val textLength = moddedPlayer.getBimoeTextLength(bimoe)

        setSize(dialogSizeX, dialogSizeY)
        backgroundPainter = BackgroundPainter.VANILLA

        add(WDialogLabel(bimoe) { nextDialog() }, 0, 0)

        // TODO: Implement load entry function
        actualEntry = """0123456789ABCDEFGHIJKLMNOPQ
                        |0123456789ABCDEFGHIJKLMNOPQ
                        |0123456789ABCDEFGHIJKLMNOPQ
                        |0123456789ABCDEFGHIJKLMNOPQ""".trimMargin()

        dialogText = WDialogText(actualEntry.take(textLength)) { nextDialog() }
        add(dialogText, 0, textOffsetY, dialogSizeX, dialogSizeY - textOffsetY)
    }

    override fun tick() {
        super.tick()

        val actual = Clock.systemUTC().millis()
        val delta = actual - lastTime
        var textLength = moddedPlayer.getBimoeTextLength(bimoe)

        if (delta > 10L && textLength < actualEntry.length) {
            if (actualEntry[textLength] == '§' && textLength + 1 < actualEntry.length) {
                textLength++
                moddedPlayer.setBimoeTextLength(bimoe, textLength)
            }

            textLength++
            moddedPlayer.setBimoeTextLength(bimoe, textLength)
            dialogText.text = LiteralText(actualEntry.take(textLength))

            lastTime = actual
        }
    }

    private fun nextDialog() {
        val textLength = moddedPlayer.getBimoeTextLength(bimoe)
        if (textLength == actualEntry.length) {
            moddedPlayer.setBimoeTextLength(bimoe, 0)
            // TODO: Implement load entry function
        }
        else {
            moddedPlayer.setBimoeTextLength(bimoe, actualEntry.length)
            dialogText.text = LiteralText(actualEntry)
        }
    }

    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        if (button == 0)
            nextDialog()
        return super.onMouseUp(x, y, button)
    }
}