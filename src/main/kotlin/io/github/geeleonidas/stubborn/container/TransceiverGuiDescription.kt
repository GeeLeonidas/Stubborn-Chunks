package io.github.geeleonidas.stubborn.container

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.StubbornInit
import io.github.geeleonidas.stubborn.client.widget.WBimoeSprite
import io.github.geeleonidas.stubborn.client.widget.WDialogBox
import io.github.geeleonidas.stubborn.client.widget.WResponseButton
import io.github.geeleonidas.stubborn.resource.DialogManager
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.math.BlockPos

class TransceiverGuiDescription(
    syncId: Int,
    playerInventory: PlayerInventory,
    pos: BlockPos
): SyncedGuiDescription(StubbornInit.transceiverHandlerType, syncId, playerInventory) {

    private val root = WPlainPanel()

    private val bimoe = Bimoe.fromBiome(world.getBiome(pos))

    private val bimoeSprite = WBimoeSprite(bimoe)
    private val dialogBox = WDialogBox(bimoe, this::callNextEntry)
    private val responseButtons = mutableListOf<WResponseButton>()

    private val playerEntity = playerInventory.player
    private val moddedPlayer = playerEntity as StubbornPlayer

    private var currentDialog = DialogManager.getDialog(bimoe, playerEntity)
        set(value) {
            if (value.id == currentDialog.id)
                return
            moddedPlayer.setCurrentEntry(bimoe, 0)
            moddedPlayer.setCurrentDialog(bimoe, value.id)
            dialogBox.dialogText.entry = value.entries[0].string
            field = value
        }

    init {
        setRootPanel(root)
        root.setSize(400, 320)

        val offsetY = 35

        root.add(bimoeSprite,
            (root.width - bimoeSprite.width) / 2,
            root.height / 2 - bimoeSprite.height + offsetY
        )

        root.add(dialogBox, (root.width - dialogBox.width) / 2, root.height / 2 + offsetY)
        dialogBox.dialogText.entry =
            currentDialog.entries[moddedPlayer.getCurrentEntry(bimoe)].string

        root.validate(this)
    }

    private fun generateResponses() {
        val responses = this.currentDialog.responses
        val nextDialogsIds = this.currentDialog.nextDialogsIds

        val buttonSizeX = 320
        val gapSizeY = 10
        val offsetY = -25
        val totalSizeY = gapSizeY * responses.size + 20 * responses.size

        for (i in responses.indices) {
            val button = WResponseButton(responses[i]) { onResponseClick(nextDialogsIds[i]) }
            responseButtons += button
            root.add(button,
                (root.width - buttonSizeX) / 2,
                (root.height - totalSizeY) / 2 + (gapSizeY * i + 20 * i) + offsetY,
                buttonSizeX, 20
            )
        }
    }

    private fun onResponseClick(toDialogId: String) {
        responseButtons.forEach { root.remove(it) }
        responseButtons.clear()
        moddedPlayer.setCurrentDialog(bimoe, toDialogId)
        currentDialog = DialogManager.getDialog(bimoe, playerEntity)
    }

    private fun callNextEntry() {
        val nextIndex = moddedPlayer.getCurrentEntry(bimoe) + 1

        if (nextIndex < currentDialog.entries.size) {
            moddedPlayer.setCurrentEntry(bimoe, nextIndex)
            dialogBox.dialogText.entry = currentDialog.entries[nextIndex].string
            return
        }

        if (currentDialog.responses.isEmpty()) {
            moddedPlayer.setCurrentDialog(bimoe, "")
            currentDialog = DialogManager.getDialog(bimoe, playerEntity)
            return
        }

        if (responseButtons.isEmpty())
            generateResponses()
    }

    override fun addPainters() {}
}