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

    fun initialize() {}

    private val generateErrorDialog = { searchId: String ->
        FeedbackDialog(
            "error", TranslatableText("dialog.${Stubborn.modId}.error", searchId)
        )
    }

    private val rootDialogs: Map<Bimoe, List<RootDialog>>
    private val nodeDialogs: Map<Bimoe, List<NodeDialog>>
    private val feedbackDialogs: Map<Bimoe, List<FeedbackDialog>>
    private val updateDialogs: Map<Bimoe, List<UpdateDialog>>
    init {
        val starterDialogs = mutableMapOf<Bimoe, List<RootDialog>>()
        val bimoeDialogs = mutableMapOf<Bimoe, List<NodeDialog>>()
        val visualDialogs = mutableMapOf<Bimoe, List<FeedbackDialog>>()
        val packetDialogs = mutableMapOf<Bimoe, List<UpdateDialog>>()
        Bimoe.values().forEach { bimoe ->
            val dialogContainer = loadBimoeDialogs(bimoe)
            starterDialogs[bimoe] = dialogContainer.rootDialogs
            bimoeDialogs[bimoe] = dialogContainer.nodeDialogs
            visualDialogs[bimoe] = dialogContainer.feedbackDialogs
            packetDialogs[bimoe] = dialogContainer.updateDialogs
        }
        rootDialogs = starterDialogs.toMap()
        nodeDialogs = bimoeDialogs.toMap()
        feedbackDialogs = visualDialogs.toMap()
        updateDialogs = packetDialogs.toMap()

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
        val playerProgress = (playerEntity as StubbornPlayer).getBimoeProgress(bimoe)
        val starterDialogs = rootDialogs[bimoe]?.filter {
            it.dialogCondition.checkFor(playerEntity) &&
            it.dialogCondition.progressNeeded <= playerProgress
        } ?: emptyList()

        return starterDialogs.minBy { it.dialogCondition.progressNeeded } ?: findDialog(bimoe, "~away")
    }

    fun findDialog(bimoe: Bimoe, id: String) =
        updateDialogs[bimoe]?.find { it.id == id } ?:
        feedbackDialogs[bimoe]?.find { it.id == id } ?:
        nodeDialogs[bimoe]?.find { it.id == id } ?: generateErrorDialog.invoke(id)

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