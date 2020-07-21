package io.github.geeleonidas.stubborn.container

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.StubbornInit
import io.github.geeleonidas.stubborn.client.widget.WBimoeSprite
import io.github.geeleonidas.stubborn.client.widget.WDialogBox
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.math.BlockPos

class TransceiverGuiDescription(
    syncId: Int,
    playerInventory: PlayerInventory,
    pos: BlockPos
): SyncedGuiDescription(StubbornInit.transceiverHandlerType, syncId, playerInventory) {

    init {
        val root = WPlainPanel()
        setRootPanel(root)
        root.setSize(400, 320)

        val bimoe = Bimoe.fromBiome(world.getBiome(pos))

        val offsetY = 35

        val sprite = WBimoeSprite(bimoe)
        root.add(sprite, (root.width - sprite.width) / 2, root.height / 2 - sprite.height + offsetY)

        val dialog = WDialogBox(bimoe, playerInventory.player)
        root.add(dialog, (root.width - dialog.width) / 2, root.height / 2 + offsetY)

        root.validate(this)
    }

    override fun addPainters() {}
}