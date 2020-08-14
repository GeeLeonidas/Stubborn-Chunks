package io.github.geeleonidas.stubborn.screen

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.StubbornInit
import io.github.geeleonidas.stubborn.client.widget.WBimoeSprite
import io.github.geeleonidas.stubborn.client.widget.WDialogBox
import io.github.geeleonidas.stubborn.client.widget.WResponseButton
import io.github.geeleonidas.stubborn.network.ChangeDialogC2SPacket
import io.github.geeleonidas.stubborn.network.NextEntryC2SPacket
import io.github.geeleonidas.stubborn.resource.DialogManager
import io.github.geeleonidas.stubborn.resource.dialog.NodeDialog
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory

abstract class AbstractTransceiverGuiDescription(
    syncId: Int,
    playerInventory: PlayerInventory
): SyncedGuiDescription(StubbornInit.transceiverHandlerType, syncId, playerInventory) {

    protected abstract val root: WPlainPanel
    abstract val bimoe: Bimoe
    protected abstract val bimoeSprite: WBimoeSprite
    protected abstract val dialogBox: WDialogBox
    protected abstract val responseButtons: MutableList<WResponseButton>
    protected abstract val playerEntity: PlayerEntity
    protected abstract val moddedPlayer: StubbornPlayer
    protected abstract var currentDialog: NodeDialog

    @Environment(EnvType.CLIENT)
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

    @Environment(EnvType.CLIENT)
    private fun onResponseClick(toDialogId: String) {
        responseButtons.forEach { root.remove(it) }
        responseButtons.clear()
        currentDialog = DialogManager.findDialog(bimoe, toDialogId)
    }

    @Environment(EnvType.CLIENT)
    protected fun callNextEntry() {
        val nextIndex = moddedPlayer.getCurrentEntry(bimoe) + 1

        if (nextIndex < currentDialog.entries.size) {
            moddedPlayer.setCurrentEntry(bimoe, nextIndex)
            NextEntryC2SPacket.sendToServer(bimoe)
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

        if (responseButtons.isEmpty())
            generateResponses()
    }
}