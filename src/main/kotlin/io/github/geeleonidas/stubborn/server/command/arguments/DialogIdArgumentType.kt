package io.github.geeleonidas.stubborn.server.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.resource.DialogManager
import net.minecraft.server.command.CommandSource
import java.util.concurrent.CompletableFuture

object DialogIdArgumentType: ArgumentType<String> {
    private val dialogExamples = listOf("death", "hungry")

    override fun parse(reader: StringReader): String {
        val start = reader.cursor

        if (!reader.canRead())
            reader.skip()
        while (reader.canRead() && reader.peek() != ' ')
            reader.skip()

        return reader.string.substring(start until reader.cursor)
    }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val bimoe = context.getArgument("bimoe", Bimoe::class.java)
        val loadedDialogs = DialogManager.getAllDialogs(bimoe)
        return CommandSource.suggestMatching(loadedDialogs.map { it.id }, builder)
    }

    override fun getExamples(): Collection<String> = dialogExamples
}