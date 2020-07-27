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

    private val bimoeSprite: WBimoeSprite
    private val dialogBox: WDialogBox
    private val responseButtons = mutableListOf<WResponseButton>()

    private val playerEntity = playerInventory.player
    private val moddedPlayer = playerEntity as StubbornPlayer
    private val bimoe: Bimoe

    private var currentDialog = DialogManager.errorDialog

    init {
        setRootPanel(root)
        root.setSize(400, 320)

        bimoe = Bimoe.fromBiome(world.getBiome(pos))

        val offsetY = 35

        bimoeSprite = WBimoeSprite(bimoe)
        root.add(bimoeSprite,
            (root.width - bimoeSprite.width) / 2,
            root.height / 2 - bimoeSprite.height + offsetY
        )

        currentDialog = DialogManager.getDialog(bimoe, playerEntity)

        dialogBox = WDialogBox(bimoe,
            currentDialog.entries[0].string,
            this::callNextEntry
        )
        root.add(dialogBox, (root.width - dialogBox.width) / 2, root.height / 2 + offsetY)

        root.validate(this)
    }

    private fun generateResponses(entrySetter: (String) -> Unit) {
        val responses = this.currentDialog.responses
        val nextDialogsIds = this.currentDialog.nextDialogsIds

        val buttonSizeX = 100
        val gapSizeY = 10
        val totalSizeY = gapSizeY * responses.size + 20 * responses.size

        for (i in responses.indices) {
            root.add(
                WResponseButton(responses[i]) { onResponseClick(nextDialogsIds[i], entrySetter) },
                (root.width - buttonSizeX) / 2, (root.height - totalSizeY) / 2 + (gapSizeY * i + 20 * i),
                buttonSizeX, 20
            )
        }
    }

    private fun onResponseClick(toDialogId: String, entrySetter: (String) -> Unit) {
        responseButtons.forEach { this.rootPanel.remove(it) }
        moddedPlayer.setCurrentEntry(bimoe, 0)
        moddedPlayer.setCurrentDialog(bimoe, toDialogId)
        currentDialog = DialogManager.getDialog(bimoe, playerEntity)
        entrySetter.invoke(currentDialog.entries[0].string)
    }

    private fun callNextEntry(entrySetter: (String) -> Unit) {
        val nextIndex = moddedPlayer.getCurrentEntry(bimoe) + 1

        if (nextIndex < currentDialog.entries.size) {
            moddedPlayer.setCurrentEntry(bimoe, nextIndex)
            entrySetter.invoke(currentDialog.entries[nextIndex].string)
            return
        }

        if (currentDialog.responses.isEmpty()) {
            moddedPlayer.setCurrentDialog(bimoe, "")
            val newDialog = DialogManager.getDialog(bimoe, playerEntity)
            if (newDialog.id != currentDialog.id) {
                moddedPlayer.setCurrentEntry(bimoe, 0)
                moddedPlayer.setCurrentDialog(bimoe, newDialog.id)
                currentDialog = newDialog
                entrySetter.invoke(currentDialog.entries[0].string)
            }
            return
        }

        if (responseButtons.isEmpty())
            generateResponses(entrySetter)
    }

    override fun addPainters() {}
}