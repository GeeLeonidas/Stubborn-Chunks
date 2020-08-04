package io.github.geeleonidas.stubborn.resource.dialog

import com.google.gson.JsonObject
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.resource.dialog.component.DialogCondition
import io.github.geeleonidas.stubborn.resource.dialog.component.EntryBimoeEffect
import io.github.geeleonidas.stubborn.resource.dialog.component.EntryTextEffect
import net.minecraft.text.TranslatableText

class RootDialog(
    id: String,
    entries: List<TranslatableText>,
    responses: List<TranslatableText>,
    nextDialogsIds: List<String>,
    entriesBimoeEffects: Map<Int, EntryBimoeEffect>,
    entriesTextEffects: Map<Int, List<EntryTextEffect>>,
    val dialogCondition: DialogCondition
): NodeDialog(id, entries, responses, nextDialogsIds, entriesBimoeEffects, entriesTextEffects) {
    companion object {
        fun fromJson(jsonObject: JsonObject, bimoe: Bimoe): RootDialog {
            val superDialog = NodeDialog.fromJson(jsonObject, bimoe)
            val condition = DialogCondition.valueOf(jsonObject["condition"].asString)

            return RootDialog(
                superDialog.id, superDialog.entries, superDialog.responses, superDialog.nextDialogsIds,
                superDialog.entriesBimoeEffects, superDialog.entriesTextEffects,
                condition
            )
        }
    }
}