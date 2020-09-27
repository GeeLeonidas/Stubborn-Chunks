package io.github.geeleonidas.stubborn.server.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.StubbornServerCommand
import io.github.geeleonidas.stubborn.network.SetProgressS2CPacket
import io.github.geeleonidas.stubborn.server.command.arguments.BimoeArgumentType
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.TranslatableText
import org.apache.logging.log4j.Level

object SetProgressCommand: StubbornServerCommand {
    override val literalBuilder: LiteralArgumentBuilder<ServerCommandSource> =
        CommandManager.literal("setprogress")
        .then(
            CommandManager.argument("bimoe", BimoeArgumentType)
            .then(
                CommandManager.argument("value", integer())
                .requires { it.hasPermissionLevel(2) }
                .executes(this)
            )
        )
    init { this.register() }

    override fun run(context: CommandContext<ServerCommandSource>): Int {
        Stubborn.log("Executing SetProgressCommand.run", Level.DEBUG)

        val playerEntity = context.source.player
        val bimoe = context.getArgument("bimoe", Bimoe::class.java)
        val progress = context.getArgument("value", Int::class.java)

        (playerEntity as StubbornPlayer).setBimoeProgress(bimoe, progress)
        SetProgressS2CPacket.sendToPlayer(playerEntity, bimoe)

        context.source.sendFeedback(
            TranslatableText(
                "commands.stubborn.set_progress.success",
                playerEntity.entityName, bimoe.capitalizedName, progress
            ),
            true
        )
        return Command.SINGLE_SUCCESS
    }
}