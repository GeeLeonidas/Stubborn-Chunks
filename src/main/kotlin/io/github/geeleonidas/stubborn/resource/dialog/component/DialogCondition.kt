package io.github.geeleonidas.stubborn.resource.dialog.component

import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

enum class DialogCondition(
    private val condition: (PlayerEntity) -> Boolean,
    val progressNeeded: Int,
    val delay: Int = 0
) {

    HUNGRY({ player -> player.hungerManager.foodLevel <= 10 }, 1),
    RAINING_SNOWING({ player -> player.world.isRaining || player.world.isThundering }, 1),

    FIRST_TIME({ true }, 0),
    DEATH_LOVER({ player -> (player as StubbornPlayer).deathCount > 99 }, 40),
    IS_TURTLE({ player -> player.inventory.contains(ItemStack(Items.TURTLE_HELMET)) }, 40);


    val isUnique = delay == 0

    fun checkFor(playerEntity: PlayerEntity) = condition.invoke(playerEntity)
}