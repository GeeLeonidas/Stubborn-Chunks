package io.github.geeleonidas.stubborn.screen

import io.github.geeleonidas.stubborn.client.widget.WResponseButton
import io.github.geeleonidas.stubborn.network.ChangeDialogC2SPacket
import io.github.geeleonidas.stubborn.network.NextEntryC2SPacket
import io.github.geeleonidas.stubborn.resource.DialogManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.math.BlockPos

class TransceiverGuiDescription(
    syncId: Int,
    playerInventory: PlayerInventory,
    pos: BlockPos
): AbstractTransceiverGuiDescription(syncId, playerInventory, pos) {

    init {
        if (world.isClient)
            this.dialogBox.setCallNextEntry(this::callNextEntry)
        this.setRootPanel(this.root)
        this.root.validate(this)
    }

    @Environment(EnvType.CLIENT)
    private fun generateResponses() {
        val responses = currentDialog.responses
        val nextDialogsIds = currentDialog.nextDialogsIds

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

    @Environment(EnvType.CLIENT)
    private fun onResponseClick(toDialogId: String) {
        responseButtons.forEach { root.remove(it) }
        responseButtons.clear()
        currentDialog = DialogManager.findDialog(bimoe, toDialogId)
    }

    @Environment(EnvType.CLIENT)
    override fun callNextEntry() {
        if (responseButtons.isNotEmpty())
            return

        val nextIndex = moddedPlayer.getCurrentEntry(bimoe) + 1

        if (nextIndex < currentDialog.entries.size) {
            moddedPlayer.setCurrentEntry(bimoe, nextIndex)
            NextEntryC2SPacket.sendToServer(bimoe)
            this.updateEntry(nextIndex)
            dialogBox.dialogText.entry = currentDialog.entries[nextIndex].string
            return
        }

        if (currentDialog.nextDialogsIds.isEmpty()) {
            moddedPlayer.setCurrentDialog(bimoe, "")
            ChangeDialogC2SPacket.sendToServer(bimoe)
            currentDialog = DialogManager.getDialog(bimoe, playerEntity)
            return
        }

        if (currentDialog.nextDialogsIds.size == 1) {
            currentDialog = DialogManager.findDialog(bimoe, currentDialog.nextDialogsIds[0])
            return
        }

        generateResponses()
    }

    override fun addPainters() {}
}