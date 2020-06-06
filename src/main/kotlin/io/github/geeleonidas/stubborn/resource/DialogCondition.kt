package io.github.geeleonidas.stubborn.resource

import io.github.geeleonidas.stubborn.util.StubbornPlayer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

enum class DialogCondition(private val condition: (PlayerEntity) -> Boolean) {
    DEATHS({ player ->
        if(player is StubbornPlayer)
            player.deathCount == 0 || player.deathCount > 99
        else
            false
    }),
    HUNGRY({ player -> player.hungerManager.foodLevel <= 10 }),
    RAINING_SNOWING({ player -> player.world.isRaining || player.world.isThundering }),
    IS_TURTLE({ player -> player.inventory.contains(ItemStack(Items.TURTLE_HELMET)) }),
    ;
    fun checkFor(playerEntity: PlayerEntity) = condition.invoke(playerEntity)
}