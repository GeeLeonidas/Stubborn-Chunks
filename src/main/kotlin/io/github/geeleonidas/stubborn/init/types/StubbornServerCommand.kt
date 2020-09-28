package io.github.geeleonidas.stubborn.init.types

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.github.geeleonidas.stubborn.Stubborn
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

interface StubbornServerCommand: Command<ServerCommandSource> {
    val literalBuilder: LiteralArgumentBuilder<ServerCommandSource>
    fun register(onlyOnDedicated: Boolean = false) {
        CommandRegistrationCallback.EVENT.register(
            CommandRegistrationCallback { dispatcher, isDedicated ->
                if (isDedicated || !onlyOnDedicated)
                    dispatcher.register(
                        CommandManager
                            .literal(Stubborn.modId)
                            .then(literalBuilder)
                    )
            }
        )
    }
}