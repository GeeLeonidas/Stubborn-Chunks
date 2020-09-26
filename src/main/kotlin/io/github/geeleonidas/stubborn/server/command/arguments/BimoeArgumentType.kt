package io.github.geeleonidas.stubborn.server.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.github.geeleonidas.stubborn.Bimoe
import net.minecraft.server.command.CommandSource
import java.util.concurrent.CompletableFuture

object BimoeArgumentType: ArgumentType<Bimoe> {
    private val bimoeSuggestions = Bimoe.values().map { it.lowerCasedName }
    private val bimoeExamples = listOf("finis", "manami")

    override fun parse(reader: StringReader) =
        Bimoe.valueOf(reader.readUnquotedString().toUpperCase())

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> =
        CommandSource.suggestMatching(bimoeSuggestions, builder)

    override fun getExamples(): Collection<String> = bimoeExamples
}