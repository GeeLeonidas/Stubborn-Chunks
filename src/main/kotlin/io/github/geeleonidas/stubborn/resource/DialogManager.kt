package io.github.geeleonidas.stubborn.resource

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.TranslatableText
import org.apache.logging.log4j.Level
import java.io.InputStreamReader

object DialogManager {

    fun initialize() {}

    val errorDialog = NodeDialog(
        "", listOf(TranslatableText("dialog.stubborn.error")), emptyList(), emptyList()
    )

    private val rootDialogs: Map<Bimoe, List<RootDialog>>
    private val nodeDialogs: Map<Bimoe, List<NodeDialog>>
    init {
        val starterDialogs = mutableMapOf<Bimoe, List<RootDialog>>()
        val bimoeDialogs = mutableMapOf<Bimoe, List<NodeDialog>>()
        Bimoe.values().forEach { bimoe ->
            val dialogPair = loadBimoeDialogs(bimoe)
            starterDialogs[bimoe] = dialogPair.first
            bimoeDialogs[bimoe] = dialogPair.second
        }
        rootDialogs = starterDialogs.toMap()
        nodeDialogs = bimoeDialogs.toMap()

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

        return starterDialogs.minBy { it.dialogCondition.progressNeeded } ?: findDialog(bimoe, "away")
    }

    private fun findDialog(bimoe: Bimoe, id: String) =
        nodeDialogs[bimoe]?.find { it.id == id } ?:
        rootDialogs[bimoe]?.find { it.id == id } ?: errorDialog

    private fun loadBimoeDialogs(bimoe: Bimoe): Pair<List<RootDialog>, List<NodeDialog>> {
        val path = Stubborn.resource("dialog/${bimoe.lowerCasedName}.json")
        val jsonStream = javaClass.getResourceAsStream(path)

        try {
            val jsonObject = jsonStream.use {
                Gson().fromJson(InputStreamReader(it, Charsets.UTF_8), JsonObject::class.java)
            }

            val rootDialogs = mutableListOf<RootDialog>()
            jsonObject["rootDialogs"].asJsonArray.forEach {
                rootDialogs += RootDialog.fromJson(it.asJsonObject, bimoe)
            }

            val nodeDialogs = mutableListOf<NodeDialog>()
            jsonObject["nodeDialogs"].asJsonArray.forEach {
                nodeDialogs += NodeDialog.fromJson(it.asJsonObject, bimoe)
            }

            return rootDialogs to nodeDialogs
        } catch (exception: Throwable) {
            exception.printStackTrace()
            return emptyList<RootDialog>() to emptyList()
        }
    }
}