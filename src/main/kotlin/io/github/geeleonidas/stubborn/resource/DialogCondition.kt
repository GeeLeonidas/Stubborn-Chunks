package io.github.geeleonidas.stubborn.resource

import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

enum class DialogCondition(
    private val condition: (PlayerEntity) -> Boolean,
    private val progressNeeded: Int,
    private val unique: Boolean = false
) {

    HUNGRY({ player -> player.hungerManager.foodLevel <= 10 }, 1),
    RAINING_SNOWING({ player -> player.world.isRaining || player.world.isThundering }, 1),

    FIRST_TIME({ true }, 0, true),
    DEATH_LOVER({ player -> (player as StubbornPlayer).deathCount > 99 }, 40, true),
    IS_TURTLE({ player -> player.inventory.contains(ItemStack(Items.TURTLE_HELMET)) }, 40, true);

    fun checkFor(playerEntity: PlayerEntity) = condition.invoke(playerEntity)
    fun getProgressNeeded() = progressNeeded
    fun isUnique() = unique
}