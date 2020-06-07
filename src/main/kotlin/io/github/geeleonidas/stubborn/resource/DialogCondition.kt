package io.github.geeleonidas.stubborn.resource

import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

enum class DialogCondition(private val condition: (PlayerEntity) -> Boolean, private val unique: Boolean = false) {

    HUNGRY({ player -> player.hungerManager.foodLevel <= 10 }),
    RAINING_SNOWING({ player -> player.world.isRaining || player.world.isThundering }),

    FIRST_TIME({ true }, true),
    DEATH_LOVER({ player -> (player as StubbornPlayer).deathCount > 99 }, true),
    IS_TURTLE({ player -> player.inventory.contains(ItemStack(Items.TURTLE_HELMET)) }, true);

    fun checkFor(playerEntity: PlayerEntity) = condition.invoke(playerEntity)
    fun isUnique() = unique
}