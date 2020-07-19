package io.github.geeleonidas.stubborn.resource

import com.google.gson.JsonObject
import io.github.geeleonidas.stubborn.Bimoe
import net.minecraft.text.TranslatableText

class RootDialog(
    id: String,
    entries: List<TranslatableText>,
    responses: List<TranslatableText>,
    nextDialogsIds: List<String>,
    val dialogCondition: DialogCondition
): NodeDialog(id, entries, responses, nextDialogsIds) {
    companion object {
        fun fromJson(jsonObject: JsonObject, bimoe: Bimoe): RootDialog {
            val superDialog = NodeDialog.fromJson(jsonObject, bimoe)
            val condition = DialogCondition.valueOf(jsonObject["condition"].asString)

            return RootDialog(
                superDialog.id, superDialog.entries, superDialog.responses, superDialog.nextDialogsIds,
                condition
            )
        }
    }
}