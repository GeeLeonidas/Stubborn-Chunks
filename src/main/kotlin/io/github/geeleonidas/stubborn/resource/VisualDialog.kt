package io.github.geeleonidas.stubborn.resource

import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import net.minecraft.text.TranslatableText

enum class VisualDialog {
    AWAY, PROGRESS_FORWARD, PROGRESS_BACKWARD;

    private val id = '~' + name.toLowerCase()
    fun generate(bimoe: Bimoe) =
        NodeDialog(id,
            listOf(TranslatableText("dialog.${Stubborn.modId}.${bimoe.lowerCasedName}.$id")),
            emptyList(), emptyList(), emptyMap(), emptyMap()
        )
}