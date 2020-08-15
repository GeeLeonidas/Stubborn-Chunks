package io.github.geeleonidas.stubborn.resource.dialog.component

import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.util.StubbornPlayer

enum class StubbornPlayerUpdate(
    private val executeLambda: (Bimoe, StubbornPlayer) -> Unit = { _,_ -> }
) {

    PROGRESS_FORWARD(
        { bimoe, moddedPlayer -> moddedPlayer.updateBimoeProgress(bimoe, 1) }
    )

    ;

    fun execute(bimoe: Bimoe, moddedPlayer: StubbornPlayer) {
        moddedPlayer.setCurrentDialog(bimoe, "") // Default instructions
        this.executeLambda.invoke(bimoe, moddedPlayer) // Enum lambda call
    }

}