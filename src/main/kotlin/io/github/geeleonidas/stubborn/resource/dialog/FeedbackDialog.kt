package io.github.geeleonidas.stubborn.resource.dialog

import com.google.gson.JsonObject
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.resource.dialog.component.EntryBimoeEffect
import net.minecraft.text.TranslatableText

open class FeedbackDialog(id: String, entry: TranslatableText):
    NodeDialog(
        id, listOf(entry), emptyList(), emptyList(),
        mapOf(0 to EntryBimoeEffect.THOUGHT), emptyMap(),
        null
    ) {
    companion object {
        fun fromJsonOrNull(jsonObject: JsonObject, bimoe: Bimoe): FeedbackDialog? {
            val hasOnlyFor = jsonObject.has("onlyFor")
            val hasExclude = jsonObject.has("exclude")
            if (hasOnlyFor xor hasExclude) {
                val jsonArray =
                    if (hasOnlyFor)
                        jsonObject["onlyFor"].asJsonArray
                    else
                        jsonObject["exclude"].asJsonArray
                var hasOccurrence = hasExclude // false when using "onlyFor", true when using "exclude"
                for (i in 0 until jsonArray.size())
                    if (jsonArray[i].asString == bimoe.name) {
                        hasOccurrence = !hasOccurrence
                        break // If there is an occurrence, invert "hasOccurrence" and then break the loop
                    }
                if (!hasOccurrence)
                    return null // Invalid Bimoe for this FeedbackDialog
            }

            val id = "~${jsonObject["id"].asString}"
            val entry = TranslatableText("dialog.${Stubborn.modId}.${bimoe.lowerCasedName}.$id")

            return FeedbackDialog(id, entry)
        }
    }
}