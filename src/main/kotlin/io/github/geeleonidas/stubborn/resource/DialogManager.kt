package io.github.geeleonidas.stubborn.resource

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.resource.dialog.FeedbackDialog
import io.github.geeleonidas.stubborn.resource.dialog.NodeDialog
import io.github.geeleonidas.stubborn.resource.dialog.RootDialog
import io.github.geeleonidas.stubborn.resource.dialog.UpdateDialog
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.TranslatableText
import org.apache.logging.log4j.Level
import java.io.InputStreamReader

object DialogManager {

    fun initialize() = Unit

    private fun generateErrorDialog(searchId: String) =
        FeedbackDialog(
            "error", TranslatableText("dialog.${Stubborn.modId}.error", searchId)
        )

    private val loadedDialogs: List<DialogContainer>
    init {
        val tempLoadedDialogs = MutableList(Bimoe.values().size) { DialogContainer.EMPTY }
        Bimoe.values().forEach { bimoe ->
            tempLoadedDialogs[bimoe.ordinal] = loadBimoeDialogs(bimoe)
        }
        loadedDialogs = tempLoadedDialogs.toList()

        Stubborn.log("ERROR 11: [ERROR_BAD_FORMAT (0xB)]", Level.WARN)
    }

    fun getDialog(bimoe: Bimoe, playerEntity: PlayerEntity): NodeDialog {
        val currentDialog = (playerEntity as StubbornPlayer).getCurrentDialog(bimoe)

        return if (currentDialog == "")
            pickNewDialog(bimoe, playerEntity)
        else
            findDialog(bimoe, currentDialog)
    }

    private fun pickNewDialog(bimoe: Bimoe, playerEntity: PlayerEntity): NodeDialog {
        val moddedPlayer = (playerEntity as StubbornPlayer)

        val playerProgress = moddedPlayer.getBimoeProgress(bimoe)
        val starterDialogs = loadedDialogs[bimoe.ordinal].root.filter {
            it.dialogCondition.checkFor(playerEntity) &&
            it.dialogCondition.progressNeeded <= playerProgress
        }

        return starterDialogs.minBy { it.dialogCondition.progressNeeded } ?:
            findDialog(bimoe, moddedPlayer.getCurrentAwayDialog(bimoe))
    }

    fun findDialog(bimoe: Bimoe, id: String) =
        loadedDialogs[bimoe.ordinal].find(id) ?: generateErrorDialog(id)

    fun getAllDialogs(bimoe: Bimoe) =
        loadedDialogs[bimoe.ordinal].getAllJoined()

    private fun loadBimoeDialogs(bimoe: Bimoe): DialogContainer {
        val bimoePath = Stubborn.resource("dialog/${bimoe.lowerCasedName}.json")
        val globalPath = Stubborn.resource("dialog/~global.json")
        val jsonBimoeStream = javaClass.getResourceAsStream(bimoePath)
        val jsonGlobalStream = javaClass.getResourceAsStream(globalPath)

        try {
            val jsonBimoeObject = jsonBimoeStream.use {
                Gson().fromJson(InputStreamReader(it, Charsets.UTF_8), JsonObject::class.java)
            }

            val rootDialogs = mutableListOf<RootDialog>()
            jsonBimoeObject["rootDialogs"].asJsonArray.forEach {
                rootDialogs += RootDialog.fromJson(it.asJsonObject, bimoe)
            }

            val nodeDialogs = mutableListOf<NodeDialog>()
            jsonBimoeObject["nodeDialogs"].asJsonArray.forEach {
                nodeDialogs += NodeDialog.fromJson(it.asJsonObject, bimoe)
            }

            val jsonFeedbackObject = jsonGlobalStream.use {
                Gson().fromJson(InputStreamReader(it, Charsets.UTF_8), JsonObject::class.java)
            }

            val feedbackDialogs = mutableListOf<FeedbackDialog>()
            jsonFeedbackObject["feedbackDialogs"].asJsonArray.forEach {
                feedbackDialogs += FeedbackDialog.fromJsonOrNull(it.asJsonObject, bimoe) ?: return@forEach
            }

            val updateDialogs = mutableListOf<UpdateDialog>()
            jsonFeedbackObject["updateDialogs"].asJsonArray.forEach {
                updateDialogs += UpdateDialog.fromJsonOrNull(it.asJsonObject, bimoe) ?: return@forEach
            }

            return DialogContainer(
                rootDialogs.toList(),
                nodeDialogs.toList(),
                feedbackDialogs.toList(),
                updateDialogs.toList()
            )
        } catch (exception: Throwable) {
            exception.printStackTrace()
            return DialogContainer.EMPTY
        }
    }
}