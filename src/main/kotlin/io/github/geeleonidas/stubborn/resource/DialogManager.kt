package io.github.geeleonidas.stubborn.resource

import com.google.gson.Gson
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import java.io.InputStreamReader

object DialogManager {

    private val errorDialog =
        Dialog(-404, listOf("Error 404: Dialog not found."), emptyMap(), DialogCondition.NONE)

    private val loadedDialogs: Map<Bimoe, List<Dialog>>
    init {
        val bimoeDialogs = mutableMapOf<Bimoe, List<Dialog>>()
        Bimoe.values().forEach { bimoeDialogs[it] = loadBimoeDialogs(it) }
        loadedDialogs = bimoeDialogs.toMap()
    }

    fun getDialog(bimoe: Bimoe, playerEntity: PlayerEntity): Dialog {
        val currentDialog = (playerEntity as StubbornPlayer).getCurrentDialog(bimoe)

        return if (currentDialog == -1)
            pickNewDialog(bimoe, playerEntity)
        else
            findDialog(bimoe, currentDialog)
    }

    private fun pickNewDialog(bimoe: Bimoe, playerEntity: PlayerEntity): Dialog {
        val playerProgress = (playerEntity as StubbornPlayer).getBimoeProgress(bimoe)
        val bimoeDialogs = loadedDialogs[bimoe]?.filter {
            it.dialogCondition.checkFor(playerEntity) &&
            it.dialogCondition.getProgressNeeded() <= playerProgress
        } ?: emptyList()

        return bimoeDialogs.minBy { it.dialogCondition.getProgressNeeded() } ?: errorDialog
    }

    private fun findDialog(bimoe: Bimoe, id: Int) =
        loadedDialogs[bimoe]?.find { it.id == id } ?: errorDialog

    private fun loadBimoeDialogs(bimoe: Bimoe): List<Dialog> {
        val path = Stubborn.resource("dialog/${bimoe.lowerCasedName()}.json")
        val jsonStream = javaClass.getResourceAsStream(path)

        return try {
            jsonStream.use {
                Gson().fromJson(
                    InputStreamReader(it, Charsets.UTF_8),
                    listOf<Dialog>().javaClass
                )
            }
        } catch (exception: Throwable) {
            exception.printStackTrace()
            emptyList()
        }
    }
}