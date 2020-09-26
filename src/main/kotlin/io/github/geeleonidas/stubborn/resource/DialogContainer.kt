package io.github.geeleonidas.stubborn.resource

import io.github.geeleonidas.stubborn.resource.dialog.FeedbackDialog
import io.github.geeleonidas.stubborn.resource.dialog.NodeDialog
import io.github.geeleonidas.stubborn.resource.dialog.RootDialog
import io.github.geeleonidas.stubborn.resource.dialog.UpdateDialog

data class DialogContainer (
    val root: List<RootDialog>,
    val node: List<NodeDialog>,
    val feedback: List<FeedbackDialog>,
    val update: List<UpdateDialog>
) {
    // TODO: Use HashMaps instead
    fun find(id: String) =
        update.find { it.id == id } ?:
        feedback.find { it.id == id } ?:
        root.find { it.id == id } ?:
        node.find { it.id == id }

    fun getAllJoined() =
        root + node + feedback + update

    companion object {
        val EMPTY = DialogContainer(
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
    }
}