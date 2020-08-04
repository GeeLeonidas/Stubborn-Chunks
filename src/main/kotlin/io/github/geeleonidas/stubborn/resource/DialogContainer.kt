package io.github.geeleonidas.stubborn.resource

import io.github.geeleonidas.stubborn.resource.dialog.FeedbackDialog
import io.github.geeleonidas.stubborn.resource.dialog.NodeDialog
import io.github.geeleonidas.stubborn.resource.dialog.RootDialog
import io.github.geeleonidas.stubborn.resource.dialog.UpdateDialog

data class DialogContainer (
    val rootDialogs: List<RootDialog>,
    val nodeDialogs: List<NodeDialog>,
    val feedbackDialogs: List<FeedbackDialog>,
    val updateDialogs: List<UpdateDialog>
) {
    companion object {
        val EMPTY = DialogContainer(
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
    }
}