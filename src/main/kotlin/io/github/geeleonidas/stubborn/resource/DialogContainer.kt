package io.github.geeleonidas.stubborn.resource

data class DialogContainer (
    val rootDialogs: List<RootDialog>,
    val nodeDialogs: List<NodeDialog>,
    val feedbackDialogs: List<FeedbackDialog>
) {
    companion object {
        val EMPTY = DialogContainer(emptyList(), emptyList(), emptyList())
    }
}