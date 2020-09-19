package io.github.geeleonidas.stubborn.resource.dialog

import com.google.gson.JsonObject
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.resource.dialog.component.StubbornPlayerUpdate
import net.minecraft.text.TranslatableText

class UpdateDialog(
    id: String,
    entry: TranslatableText,
    val playerUpdate: StubbornPlayerUpdate
): FeedbackDialog(id, entry) {
    companion object {
        fun fromJsonOrNull(jsonObject: JsonObject, bimoe: Bimoe): UpdateDialog? {
            val feedbackDialog =
                FeedbackDialog.fromJsonOrNull(jsonObject, bimoe) ?: return null

            val playerUpateEnum = StubbornPlayerUpdate.valueOf(jsonObject["packet"].asString)

            return UpdateDialog(
                feedbackDialog.id,
                feedbackDialog.entries.first(),
                playerUpateEnum
            )
        }
    }
}