package io.github.geeleonidas.stubborn.resource

import com.google.gson.Gson
import io.github.geeleonidas.stubborn.Bimoe
import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import java.io.InputStreamReader

object DialogManager {

    private val loadedDialogs: Map<Bimoe, List<Dialog>>
    init {
        val bimoeDialogs = mutableMapOf<Bimoe, List<Dialog>>()
        Bimoe.values().forEach { bimoeDialogs[it] = loadBimoeDialogs(it) }
        loadedDialogs = bimoeDialogs.toMap()
    }

    fun getDialog(bimoe: Bimoe, playerEntity: PlayerEntity) {
        val currentDialog = (playerEntity as StubbornPlayer).getCurrentDialog(bimoe)

        if (currentDialog == -1)
            pickNewDialog(bimoe, playerEntity)
        else
            findDialog(bimoe, currentDialog)
    }

    private fun pickNewDialog(bimoe: Bimoe, playerEntity: PlayerEntity) {
        // Picks a new random dialog based on DialogCondition and Bimoe progress
    }

    private fun findDialog(bimoe: Bimoe, id: Int) =
        loadedDialogs[bimoe]?.find { it.id == id }

    private fun loadBimoeDialogs(bimoe: Bimoe): List<Dialog> {
        val path = Stubborn.resource("dialog/${bimoe.lowerCasedName()}.json")
        val jsonStream = javaClass.getResourceAsStream(path)

        val listOfDialogs =
            try {
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

        Stubborn.log("Dialogs for ${bimoe.capitalizedName()} are now loaded.")
        return listOfDialogs
    }
}