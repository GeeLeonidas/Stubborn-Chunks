package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WText
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting
import java.sql.Time
import java.time.Clock
import java.time.Instant
import java.util.*
import java.util.function.Supplier

class WDialogBox(
    private val bimoe: Stubborn.Bimoe,
    private val moddedPlayer: StubbornPlayer
): WPlainPanel() {

    private var actualEntry = ""
    private var lastTime = Clock.systemUTC().millis()
    private val dialogText: WText

    init {
        setSize(dialogSizeX, dialogSizeY)
        backgroundPainter = BackgroundPainter.VANILLA

        // WLabel TODO: Translate this mess into a widget class
        val name = object: WLabel(LiteralText(bimoe.name.toLowerCase().capitalize()).
            formatted(Formatting.BOLD).formatted(selectBimoeFormat(bimoe))) {
            override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
                nextDialog()
                return super.onMouseUp(x, y, button)
            }
        }
        add(name, 0, 0)

        // Entry
        // TODO: Implement load entry function
        actualEntry = "0123456789ABCDEFGHIJKLMNOPQ0123456789ABCDEFGHIJKLMNOPQ0123456789ABCDEFGHIJKLMNOPQ0123456789ABCDEFGHIJKLMNOPQ"

        // WText TODO: Translate this mess into a widget class
        val offsetY = 14
        val textLength = moddedPlayer.getBimoeTextLength(bimoe)
        dialogText = object: WText(LiteralText(actualEntry.take(textLength))) {
            override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
                nextDialog()
                return super.onMouseUp(x, y, button)
            }
        }
        add(dialogText, 0, offsetY, dialogSizeX, dialogSizeY - offsetY)
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

private const val dialogSizeX = 160
private const val dialogSizeY =  50

// TODO: Gather all util functions into one single object
private fun selectBimoeFormat(bimoe: Stubborn.Bimoe): Formatting {
    return when(bimoe) {
        Stubborn.Bimoe.SILVIS -> Formatting.DARK_GREEN
        Stubborn.Bimoe.FINIS -> Formatting.BLACK
        Stubborn.Bimoe.SORBIRE -> Formatting.DARK_PURPLE
        Stubborn.Bimoe.ERIMOS -> Formatting.GOLD
        Stubborn.Bimoe.LAVINA -> Formatting.DARK_BLUE
        Stubborn.Bimoe.MANAMI -> Formatting.DARK_AQUA
    }
}