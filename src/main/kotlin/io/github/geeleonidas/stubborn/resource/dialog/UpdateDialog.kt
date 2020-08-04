package io.github.geeleonidas.stubborn.resource.dialog

import com.google.gson.JsonObject
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.StubbornC2SPacket
import io.github.geeleonidas.stubborn.resource.dialog.component.DialogPacket
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.text.TranslatableText

class UpdateDialog(
    id: String,
    entry: TranslatableText,
    val packet: StubbornC2SPacket,
    val executeUpdate: (Bimoe, StubbornPlayer) -> Unit
):
    FeedbackDialog(id, entry) {
    companion object {
        fun fromJsonOrNull(jsonObject: JsonObject, bimoe: Bimoe): UpdateDialog? {
            val feedbackDialog =
                FeedbackDialog.fromJsonOrNull(jsonObject, bimoe) ?: return null

            val packetEnum = DialogPacket.valueOf(jsonObject["packet"].asString)

            return UpdateDialog(
                feedbackDialog.id,
                feedbackDialog.entries.first(),
                packetEnum.stubbornPacket,
                packetEnum.execute
            )
        }
    }
}