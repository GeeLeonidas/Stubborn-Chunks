package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.widget.WSprite
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import org.apache.logging.log4j.Level

class WBimoeSprite(bimoe: Bimoe):
    WSprite(Stubborn.makeId("textures/bimoe/${bimoe.lowerCasedName}/default.png")) {

    val defaultSprite: Identifier = super.frames[0]
    var visible = true

    @Environment(EnvType.CLIENT)
    override fun paint(matrices: MatrixStack?, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        if (!visible)
            return
        super.paint(matrices, x, y, mouseX, mouseY)
    }

    @Environment(EnvType.CLIENT)
    override fun setImage(image: Identifier): WSprite {
        if (MinecraftClient.getInstance().textureManager.getTexture(image) == null) {
            Stubborn.log("Bimoe texture '${this.frames[0]}' not found!", Level.WARN)
            return super.setImage(defaultSprite)
        }

        return super.setImage(image)
    }

    override fun canResize() = false

    override fun getHeight() = 128
    override fun getWidth() = 128
}