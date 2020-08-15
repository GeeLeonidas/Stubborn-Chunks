package io.github.geeleonidas.stubborn.screen

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.StubbornInit
import io.github.geeleonidas.stubborn.client.widget.WBimoeSprite
import io.github.geeleonidas.stubborn.client.widget.WDialogBox
import io.github.geeleonidas.stubborn.client.widget.WResponseButton
import io.github.geeleonidas.stubborn.network.ChangeDialogC2SPacket
import io.github.geeleonidas.stubborn.network.UpdatePlayerC2SPacket
import io.github.geeleonidas.stubborn.resource.DialogManager
import io.github.geeleonidas.stubborn.resource.dialog.FeedbackDialog
import io.github.geeleonidas.stubborn.resource.dialog.UpdateDialog
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.math.BlockPos

abstract class AbstractTransceiverGuiDescription(
    syncId: Int,
    playerInventory: PlayerInventory,
    pos: BlockPos
): SyncedGuiDescription(StubbornInit.transceiverHandlerType, syncId, playerInventory) {

    val bimoe = Bimoe.fromBiome(world.getBiome(pos))

    protected val root = WPlainPanel()

    protected val bimoeSprite = WBimoeSprite(bimoe)
    protected val dialogBox = WDialogBox(bimoe)
    protected val responseButtons = mutableListOf<WResponseButton>()

    protected val playerEntity: PlayerEntity = playerInventory.player
    protected val moddedPlayer = playerEntity as StubbornPlayer

    @Environment(EnvType.CLIENT)
    protected var currentDialog = DialogManager.getDialog(bimoe, playerEntity)
        set(value) {
            if (value.id == currentDialog.id)
                return
            if (value !is FeedbackDialog) {
                moddedPlayer.setCurrentDialog(bimoe, value.id)
                ChangeDialogC2SPacket.sendToServer(bimoe)
            } else {
                if (value is UpdateDialog) {
                    value.playerUpdate.execute(bimoe, moddedPlayer)
                    UpdatePlayerC2SPacket.sendToServer(bimoe)
                }
            }
            field = value
            applyEntryEffects(0)
            dialogBox.dialogText.entry = value.entries[0].string
        }

    init {
        root.setSize(400, 320)

        val offsetY = 35

        root.add(bimoeSprite,
            (root.width - bimoeSprite.width) / 2,
            root.height / 2 - bimoeSprite.height + offsetY
        )

        root.add(dialogBox, (root.width - dialogBox.width) / 2, root.height / 2 + offsetY)
        if (world.isClient())
            dialogBox.dialogText.entry =
                currentDialog.entries[moddedPlayer.getCurrentEntry(bimoe)].string
    }

    protected abstract fun applyEntryEffects(nextIndex: Int)
    protected abstract fun callNextEntry()

}