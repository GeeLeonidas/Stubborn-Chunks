package io.github.geeleonidas.stubborn.resource

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import java.io.InputStreamReader
import kotlin.reflect.typeOf

object DialogManager {

    private val errorDialog =
        NodeDialog(-404, listOf("Error 404: Dialog not found."), emptyMap())

    private val rootDialogs: Map<Bimoe, List<RootDialog>>
    private val loadedDialogs: Map<Bimoe, List<NodeDialog>>
    init {
        val bimoeDialogs = mutableMapOf<Bimoe, List<NodeDialog>>()
        val starterDialogs = mutableMapOf<Bimoe, List<RootDialog>>()
        Bimoe.values().forEach { bimoe ->
            bimoeDialogs[bimoe] = loadBimoeDialogs(bimoe)
            starterDialogs[bimoe] = bimoeDialogs[bimoe]!!.filterIsInstance<RootDialog>()
        }
        loadedDialogs = bimoeDialogs.toMap()
        rootDialogs = starterDialogs.toMap()
    }

    fun getDialog(bimoe: Bimoe, playerEntity: PlayerEntity): NodeDialog {
        val currentDialog = (playerEntity as StubbornPlayer).getCurrentDialog(bimoe)

        return if (currentDialog == -1)
            pickNewDialog(bimoe, playerEntity)
        else
            findDialog(bimoe, currentDialog)
    }

    private fun pickNewDialog(bimoe: Bimoe, playerEntity: PlayerEntity): NodeDialog {
        val playerProgress = (playerEntity as StubbornPlayer).getBimoeProgress(bimoe)
        val starterDialogs = rootDialogs[bimoe]?.filter {
            it.dialogCondition.checkFor(playerEntity) &&
            it.dialogCondition.getProgressNeeded() <= playerProgress
        } ?: emptyList()

        return starterDialogs.minBy { it.dialogCondition.getProgressNeeded() } ?: errorDialog
    }

    private fun findDialog(bimoe: Bimoe, id: Int) =
        loadedDialogs[bimoe]?.find { it.id == id } ?: errorDialog

    private fun loadBimoeDialogs(bimoe: Bimoe): List<NodeDialog> {
        val path = Stubborn.resource("dialog/${bimoe.lowerCasedName()}.json")
        val jsonStream = javaClass.getResourceAsStream(path)

        return try {
            jsonStream.use {
                Gson().fromJson(
                    InputStreamReader(it, Charsets.UTF_8),
                    TypeToken.getParameterized(List::class.java, NodeDialog::class.java).type
                )
            }
        } catch (exception: Throwable) {
            exception.printStackTrace()
            emptyList()
        }
    }
}