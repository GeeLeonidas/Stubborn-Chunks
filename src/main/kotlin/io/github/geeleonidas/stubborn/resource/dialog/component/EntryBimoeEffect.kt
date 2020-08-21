package io.github.geeleonidas.stubborn.resource.dialog.component

import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.client.widget.WBimoeSprite
import io.github.geeleonidas.stubborn.client.widget.WDialogBox

enum class EntryBimoeEffect(
    private val lambdaBimoeEffect: (Bimoe, WBimoeSprite, WDialogBox) -> Unit = { _,_,_ -> },
    private val isAway: Boolean = false
) {

    DEFAULT,
    THOUGHT(isAway = true),
    HAPPY,
    SAD,
    ANGRY,
    SURPRISED

    ;

    fun apply(bimoe: Bimoe, bimoeSprite: WBimoeSprite, dialogBox: WDialogBox) {
        // Visible settings
        bimoeSprite.visible = !isAway
        dialogBox.dialogLabel.visible = !isAway
        dialogBox.dialogText.isTypingSoundMuted = isAway
        // Lambda call
        lambdaBimoeEffect.invoke(bimoe, bimoeSprite, dialogBox)
    }

}