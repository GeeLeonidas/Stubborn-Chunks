package io.github.geeleonidas.stubborn.resource.dialog.component

enum class EntryTextEffect(val typingModifier: Float = 1f) {

    SPAMMING(3f), FAST_WRITING(2f),
    SPELLING(1/3f), SLOW_WRITING(1/2f)

}