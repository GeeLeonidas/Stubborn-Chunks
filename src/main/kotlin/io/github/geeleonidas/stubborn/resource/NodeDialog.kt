package io.github.geeleonidas.stubborn.resource

import com.google.gson.JsonObject
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import net.minecraft.text.TranslatableText

open class NodeDialog(
    val id: String,
    val entries: List<TranslatableText>,
    val responses: List<TranslatableText>,
    val nextDialogsIds: List<String>
) {
    companion object {
        fun fromJson(jsonObject: JsonObject, bimoe: Bimoe): NodeDialog {
            val id = jsonObject["id"].asString
            val count = jsonObject["entryListSize"].asInt
            val entries = mutableListOf<TranslatableText>()

            if (count > 1)
                for (i in 1..count)
                    entries += TranslatableText(
                        "dialog.${Stubborn.modId}.${bimoe.lowerCasedName}.$id.$i"
                    )
            else
                entries += TranslatableText("dialog.${Stubborn.modId}.${bimoe.lowerCasedName}.$id")

            val nextDialogsIds = mutableListOf<String>()
            val responseTexts = mutableListOf<TranslatableText>()

            for (response in jsonObject["responses"].asJsonArray) {
                val responseAndPointer = response.asString.split("->")

                val responseStr = responseAndPointer.first()
                val pointerStr = responseAndPointer.
                    elementAtOrNull(1) ?: "${id}_${responseStr}"

                nextDialogsIds += pointerStr
                responseTexts += TranslatableText(
                    "response.${Stubborn.modId}.${bimoe.lowerCasedName}.$id.$responseStr"
                )
            }

            return NodeDialog(id, entries, responseTexts, nextDialogsIds)
        }
    }
}