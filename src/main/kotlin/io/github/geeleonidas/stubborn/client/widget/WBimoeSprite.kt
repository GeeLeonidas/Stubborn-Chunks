package io.github.geeleonidas.stubborn.client.widget

import io.github.cottonmc.cotton.gui.widget.WSprite
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn

class WBimoeSprite(bimoe: Bimoe):
    WSprite(Stubborn.makeId("textures/bimoe/${bimoe.name.toLowerCase()}.png")) {

    override fun canResize() = false

    override fun getHeight() = 128
    override fun getWidth() = 128
}