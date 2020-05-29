package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WText
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
    private val moddedPlayer: StubbornPlayer,
    private val actualTick: () -> Long
): WPlainPanel() {

    private var actualEntry = ""
    private var entryLength = 0
    private var lastTick = actualTick.invoke()
    private val dialogText: WText

    init {
        setSize(dialogSizeX, dialogSizeY)
        backgroundPainter = BackgroundPainter.VANILLA

        // WLabel
        val name = WLabel(
            LiteralText(bimoe.name.toLowerCase().capitalize()).
            formatted(Formatting.BOLD).formatted(selectBimoeFormat(bimoe))
        )
        add(name, 0, 0)

        // Entry
        // TODO: load entry function
        actualEntry = "0123456789ABCDEFGHIJKLMNOPQ0123456789ABCDEFGHIJKLMNOPQ0123456789ABCDEFGHIJKLMNOPQ0123456789ABCDEFGHIJKLMNOPQ"

        // WText
        val offsetY = 14
        dialogText = WText(LiteralText(actualEntry.take(entryLength)))
        add(dialogText, 0, offsetY, dialogSizeX, dialogSizeY - offsetY)
    }

    override fun tick() {
        super.tick()

        val actual = actualTick.invoke()
        val delta = actual - lastTick

        if (delta > 0L && entryLength < actualEntry.length) {
            entryLength++
            dialogText.text = LiteralText(actualEntry.take(entryLength))
            lastTick = actual
        }
    }

    private fun nextDialog() {
        entryLength = actualEntry.length

        // TODO: Implement dialog structure
    }

    override fun onClick(x: Int, y: Int, button: Int) {
        nextDialog()
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