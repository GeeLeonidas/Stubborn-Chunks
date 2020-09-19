package io.github.geeleonidas.stubborn.resource.dialog

import com.google.gson.JsonObject
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.resource.dialog.component.EntryBimoeEffect
import io.github.geeleonidas.stubborn.resource.dialog.component.EntryTextEffect
import net.minecraft.text.TranslatableText

open class NodeDialog(
    val id: String,
    val entries: List<TranslatableText>,
    val responses: List<TranslatableText>,
    val nextDialogsIds: List<String>,
    val entriesBimoeEffects: Map<Int, EntryBimoeEffect>,
    val entriesTextEffects: Map<Int, List<EntryTextEffect>>,
    val changeAwayTo: String?
) {
    companion object {
        fun fromJson(dialogObject: JsonObject, bimoe: Bimoe): NodeDialog {
            val id = dialogObject["id"].asString
            val count = dialogObject["entryListSize"].asInt
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

            val responseJsonArray = dialogObject["responses"].asJsonArray
            if (responseJsonArray.size() != 1)
                for (response in responseJsonArray) {
                    val responseAndPointer = response.asString.split("->")

                    val responseStr = responseAndPointer.first()
                    val pointerStr = responseAndPointer.
                        elementAtOrNull(1) ?: "${id}_${responseStr}"

                    nextDialogsIds += pointerStr
                    responseTexts += TranslatableText(
                        "response.${Stubborn.modId}.${bimoe.lowerCasedName}.$id.$responseStr"
                    )
                }
            else // One-element arrays always generate a pointer without a TranslatableText entry
                nextDialogsIds += responseJsonArray[0].asString

            val entriesBimoeEffects = mutableMapOf<Int, EntryBimoeEffect>()
            val entriesTextEffects = mutableMapOf<Int,List<EntryTextEffect>>()

            if (dialogObject.has("entriesEffects"))
                for ((key,value) in dialogObject["entriesEffects"].asJsonObject.entrySet()) {
                    val entryIndex = key.toInt() - 1
                    val entryEffectArray = value.asJsonArray

                    val bimoeEffectName = entryEffectArray.first().asString
                    if (bimoeEffectName != "DEFAULT")
                        entriesBimoeEffects[entryIndex] = EntryBimoeEffect.valueOf(bimoeEffectName)

                    val tempEffectList = mutableListOf<EntryTextEffect>()
                    for (i in 1 until entryEffectArray.size())
                        tempEffectList += EntryTextEffect.valueOf(entryEffectArray[i].asString)

                    if (tempEffectList.isNotEmpty())
                        entriesTextEffects[entryIndex] = tempEffectList
                }

            var changeAwayTo: String? = null
            if (dialogObject.has("changeAwayTo"))
                changeAwayTo = dialogObject["changeAwayTo"].asString

            return NodeDialog(
                id, entries, responseTexts, nextDialogsIds,
                entriesBimoeEffects, entriesTextEffects,
                changeAwayTo
            )
        }
    }
}