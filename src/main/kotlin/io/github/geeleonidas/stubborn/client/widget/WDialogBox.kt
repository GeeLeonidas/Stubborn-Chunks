package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.resource.DialogManager
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity

class WDialogBox(
    private val bimoe: Bimoe,
    private val playerEntity: PlayerEntity
): WPlainPanel() {

    private val moddedPlayer
        get() = playerEntity as StubbornPlayer

    // TODO: Move this up in the hierarchy
    private var currentDialog = DialogManager.errorDialog
        set(value) {
            moddedPlayer.setCurrentDialog(bimoe, value.id)
            moddedPlayer.setCurrentEntry(bimoe, 0)
            field = value
        }

    private val dialogLabel: WDialogLabel
    private val dialogText: WDialogText

    init {
        val dialogSizeX = 160
        val dialogSizeY = 50
        val textOffsetY = 14

        setSize(dialogSizeX, dialogSizeY)
        backgroundPainter = BackgroundPainter.VANILLA

        dialogLabel = WDialogLabel(bimoe) { nextDialog() }
        add(dialogLabel, 0, 0)

        dialogText = WDialogText { nextDialog() }
        add(dialogText, 0, textOffsetY, dialogSizeX, dialogSizeY - textOffsetY)

        currentDialog = DialogManager.getDialog(bimoe, playerEntity)
        dialogText.entry = getCurrentEntry()
    }

    private fun nextDialog() {
        if (dialogText.isFinished()) {
            val newEntry = moddedPlayer.getCurrentEntry(bimoe) + 1
            if (newEntry >= currentDialog.entries.size) {
                moddedPlayer.setCurrentDialog(bimoe, "")
                currentDialog = DialogManager.getDialog(bimoe, playerEntity)
                return
            }

            moddedPlayer.setCurrentEntry(bimoe, newEntry)
            // TODO: Fix upcoming dialogs
            dialogText.entry = getCurrentEntry()
            return
        }

        dialogText.finish()
    }

    private fun getCurrentEntry() =
        currentDialog.entries[moddedPlayer.getCurrentEntry(bimoe)].string

    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        if (button == 0)
            nextDialog()
        return super.onMouseUp(x, y, button)
    }
}