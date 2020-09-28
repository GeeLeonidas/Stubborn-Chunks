package io.github.geeleonidas.stubborn.server.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.init.types.StubbornServerCommand
import io.github.geeleonidas.stubborn.network.SetDialogS2CPacket
import io.github.geeleonidas.stubborn.resource.DialogManager
import io.github.geeleonidas.stubborn.server.command.arguments.BimoeArgumentType
import io.github.geeleonidas.stubborn.server.command.arguments.DialogIdArgumentType
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.TranslatableText
import org.apache.logging.log4j.Level

object SetDialogCommand: StubbornServerCommand {
    override val literalBuilder: LiteralArgumentBuilder<ServerCommandSource> =
        CommandManager.literal("setdialog")
        .then(
            CommandManager.argument("bimoe", BimoeArgumentType)
            .then(
                CommandManager.argument("dialogId", DialogIdArgumentType)
                .requires { it.hasPermissionLevel(2) }
                .executes(this)
            )
        )

    override fun run(context: CommandContext<ServerCommandSource>): Int {
        Stubborn.log("Executing SetDialogCommand.run", Level.DEBUG)

        val playerEntity = context.source.player
        val bimoe = context.getArgument("bimoe", Bimoe::class.java)
        val dialogId = context.getArgument("dialogId", String::class.java)
        if (DialogManager.findDialog(bimoe, dialogId).id == "error") {
            context.source.sendError(
                TranslatableText("commands.stubborn.set_dialog.not_found", bimoe.capitalizedName, dialogId)
            )
            return Command.SINGLE_SUCCESS
        }

        (playerEntity as StubbornPlayer).setCurrentDialog(bimoe, dialogId)
        SetDialogS2CPacket.sendToPlayer(context.source.player, bimoe)

        context.source.sendFeedback(
            TranslatableText(
                "commands.stubborn.set_dialog.success",
                playerEntity.entityName, bimoe.capitalizedName, dialogId
            ),
            true
        )
        return Command.SINGLE_SUCCESS
    }
}