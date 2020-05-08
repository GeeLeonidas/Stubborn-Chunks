package io.github.geeleonidas.stubborn.client

import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.geeleonidas.stubborn.block.entity.TransceiverBlockEntity

class TransceiverGui(val ref: TransceiverBlockEntity): LightweightGuiDescription() {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setSize(300, 200)

        TODO("Buttons and packets")

        root.validate(this)
    }
}

class TransceiverScreen(ref: TransceiverBlockEntity): CottonClientScreen(TransceiverGui(ref))