package io.github.geeleonidas.stubborn.resource

data class Dialog(
    val id: Int,
    val entries: List<String>,
    val responses: Map<String, Int>,
    val dialogCondition: DialogCondition
)