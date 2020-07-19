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
            val entries = mutableListOf<TranslatableText>()
            for (i in 1..jsonObject["entries"].asInt) {
                entries += TranslatableText(
                    "dialog.${Stubborn.modId}.${bimoe.lowerCasedName()}.$id.$i"
                )
            }

            val nextDialogsIds = mutableListOf<String>()
            val responses = mutableListOf<TranslatableText>()
            for (response in jsonObject["responses"].asJsonArray) {
                val responseStr = response.asString
                nextDialogsIds += "${id}_$responseStr"
                responses += TranslatableText(
                    "response.${Stubborn.modId}.${bimoe.lowerCasedName()}.$id.$responseStr"
                )
            }

            return NodeDialog(id, entries, responses, nextDialogsIds)
        }
    }
}