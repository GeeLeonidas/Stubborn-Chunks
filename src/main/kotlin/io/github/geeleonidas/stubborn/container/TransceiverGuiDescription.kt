package io.github.geeleonidas.stubborn.container

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.StubbornInit
import io.github.geeleonidas.stubborn.client.widget.WBimoeSprite
import io.github.geeleonidas.stubborn.client.widget.WDialogBox
import io.github.geeleonidas.stubborn.client.widget.WResponseButton
import io.github.geeleonidas.stubborn.resource.DialogManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.math.BlockPos

class TransceiverGuiDescription(
    syncId: Int,
    playerInventory: PlayerInventory,
    pos: BlockPos
): SyncedGuiDescription(StubbornInit.transceiverHandlerType, syncId, playerInventory) {

    private val bimoeSprite: WBimoeSprite
    private val dialogBox: WDialogBox
    private val responseButtons = mutableListOf<WResponseButton>()

    private val playerEntity: PlayerEntity
    private val bimoe: Bimoe

    private var currentDialog = DialogManager.errorDialog

    init {
        val root = WPlainPanel()
        setRootPanel(root)
        root.setSize(400, 320)

        bimoe = Bimoe.fromBiome(world.getBiome(pos))
        playerEntity = playerInventory.player

        val offsetY = 35

        bimoeSprite = WBimoeSprite(bimoe)
        root.add(bimoeSprite,
            (root.width - bimoeSprite.width) / 2,
            root.height / 2 - bimoeSprite.height + offsetY
        )

        dialogBox = WDialogBox(bimoe, playerEntity)
        root.add(dialogBox, (root.width - dialogBox.width) / 2, root.height / 2 + offsetY)

        root.validate(this)
    }

    private fun generateResponses() {
        // TODO: Plan on how the button arrangement is going to be done
    }

    private fun onResponseClick(toDialogId: String) {
        responseButtons.forEach { this.rootPanel.remove(it) }
        currentDialog = DialogManager.getDialog(bimoe, playerEntity)
    }

    override fun addPainters() {}
}