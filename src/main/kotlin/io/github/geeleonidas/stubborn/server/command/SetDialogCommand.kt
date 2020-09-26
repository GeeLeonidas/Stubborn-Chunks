package io.github.geeleonidas.stubborn.server.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.context.CommandContext
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.StubbornServerCommand
import io.github.geeleonidas.stubborn.network.SetDialogS2CPacket
import io.github.geeleonidas.stubborn.resource.DialogManager
import io.github.geeleonidas.stubborn.server.command.arguments.BimoeArgumentType
import io.github.geeleonidas.stubborn.server.command.arguments.DialogIdArgumentType
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import org.apache.logging.log4j.Level

object SetDialogCommand: StubbornServerCommand {
    init { this.register() }

    override val literalBuilder: LiteralArgumentBuilder<ServerCommandSource> =
        CommandManager.literal("setdialog")
            .requires { it.hasPermissionLevel(2) }
            .then(
                argument<ServerCommandSource, Bimoe>("bimoe", BimoeArgumentType)
                .then(
                    argument<ServerCommandSource, String>("dialogId", DialogIdArgumentType)
                    .executes(this)
                )
            )

    override fun run(context: CommandContext<ServerCommandSource>): Int {
        Stubborn.log("Executing SetDialogCommand.run", Level.DEBUG)
        
        val bimoe = context.getArgument("bimoe", Bimoe::class.java)
        val dialogId = context.getArgument("dialogId", String::class.java)
        if (DialogManager.findDialog(bimoe, dialogId).id == "error") {
            // TODO: Error message for not finding the dialog
            return Command.SINGLE_SUCCESS
        }

        (context.source.player as StubbornPlayer).setCurrentDialog(bimoe, dialogId)
        SetDialogS2CPacket.sendToPlayer(context.source.player, bimoe)
        return Command.SINGLE_SUCCESS
    }
}