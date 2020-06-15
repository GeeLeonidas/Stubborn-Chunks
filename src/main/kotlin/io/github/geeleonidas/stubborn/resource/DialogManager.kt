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
        val starterDialogs = mutableMapOf<Bimoe, List<RootDialog>>()
        val bimoeDialogs = mutableMapOf<Bimoe, List<NodeDialog>>()
        Bimoe.values().forEach { bimoe ->
            val dialogPair = loadBimoeDialogs(bimoe)
            starterDialogs[bimoe] = dialogPair.first
            bimoeDialogs[bimoe] = dialogPair.second
        }
        rootDialogs = starterDialogs.toMap()
        loadedDialogs = bimoeDialogs.toMap()
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

    private fun loadBimoeDialogs(bimoe: Bimoe): Pair<List<RootDialog>, List<NodeDialog>> {
        val path = Stubborn.resource("dialog/${bimoe.lowerCasedName()}.json")
        val jsonStream = javaClass.getResourceAsStream(path)

        val rootDialogList = TypeToken.getParameterized(List::class.java, NodeDialog::class.java).type
        val nodeDialogList = TypeToken.getParameterized(List::class.java, NodeDialog::class.java).type

        return try {
            jsonStream.use {
                Gson().fromJson(
                    InputStreamReader(it, Charsets.UTF_8),
                    TypeToken.getParameterized(Pair::class.java, rootDialogList, nodeDialogList).type
                )
            }
        } catch (exception: Throwable) {
            exception.printStackTrace()
            emptyList<RootDialog>() to emptyList()
        }
    }
}