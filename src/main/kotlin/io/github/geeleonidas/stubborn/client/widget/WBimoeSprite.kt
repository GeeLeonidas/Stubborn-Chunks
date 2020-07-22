package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.widget.WSprite
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import net.minecraft.client.util.math.MatrixStack

class WBimoeSprite(bimoe: Bimoe):
    WSprite(Stubborn.makeId("textures/bimoe/${bimoe.lowerCasedName()}.png")) {

    var visible = true

    override fun paint(matrices: MatrixStack?, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        if (!visible)
            return
        super.paint(matrices, x, y, mouseX, mouseY)
    }

    override fun canResize() = false

    override fun getHeight() = 128
    override fun getWidth() = 128
}