package io.github.geeleonidas.stubborn.resource

open class NodeDialog(
    val id: Int,
    val entries: List<String>,
    val responses: Map<String, Int>
)

class RootDialog(
    id: Int,
    entries: List<String>,
    responses: Map<String, Int>,
    val dialogCondition: DialogCondition
): NodeDialog(id, entries, responses)