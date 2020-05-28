package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WText
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting

class WDialogBox(private val bimoe: Stubborn.Bimoe, private val moddedPlayer: StubbornPlayer):
    WPlainPanel() {

    private val wtext: WText

    init {
        setSize(dialogSizeX, dialogSizeY)
        backgroundPainter = BackgroundPainter.VANILLA

        val name = WLabel(
            LiteralText(bimoe.name.toLowerCase().capitalize()).
            formatted(Formatting.BOLD).formatted(selectBimoeFormat(bimoe))
        )
        add(name, 0, 0)

        val offsetY = 14
        wtext = WText(LiteralText("0123456789ABCDEFGHIJKLMNOPQ0123456789ABCDEFGHIJKLMNOPQ0123456789ABCDEFGHIJKLMNOPQ0123456789ABCDEFGHIJKLMNOPQ"))
        add(wtext, 0, offsetY, dialogSizeX, dialogSizeY - offsetY)
    }

    private fun nextDialog() {
        TODO("Implement dialog structure")
    }

    override fun onClick(x: Int, y: Int, button: Int) {
        nextDialog()
    }
}

private const val dialogSizeX = 160
private const val dialogSizeY =  50

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