package io.github.geeleonidas.stubborn.resource.dialog.component

import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.StubbornC2SPacket
import io.github.geeleonidas.stubborn.network.UpdateProgressC2SPacket
import io.github.geeleonidas.stubborn.util.StubbornPlayer

enum class DialogPacket(
    val stubbornPacket: StubbornC2SPacket,
    executeLambda: (Bimoe, StubbornPlayer) -> Unit = { _,_ -> }
) {
    PROGRESS_FORWARD(
        UpdateProgressC2SPacket,
        { bimoe, moddedPlayer -> moddedPlayer.updateBimoeProgress(bimoe, 1) }
    ),
    PROGRESS_BACKWARD(
        UpdateProgressC2SPacket,
        { bimoe, moddedPlayer -> moddedPlayer.updateBimoeProgress(bimoe, 0) }
    )
    ;
    val execute = { bimoe: Bimoe, moddedPlayer: StubbornPlayer ->
        moddedPlayer.setCurrentDialog(bimoe, "") // Default instructions
        executeLambda.invoke(bimoe, moddedPlayer) // Enum lambda call
    }
}