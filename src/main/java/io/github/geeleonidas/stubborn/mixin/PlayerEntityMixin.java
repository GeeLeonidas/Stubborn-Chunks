package io.github.geeleonidas.stubborn.mixin;

import io.github.geeleonidas.stubborn.Bimoe;
import io.github.geeleonidas.stubborn.util.StubbornPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(PlayerEntity.class)
abstract public class PlayerEntityMixin implements StubbornPlayer {
    private final HashMap<Bimoe, Integer> bimoeProgress = new HashMap<>();
    private final HashMap<Bimoe, Integer> bimoeTextLength = new HashMap<>();

    @Override
    public int getBimoeProgress(Bimoe bimoe) {
        return bimoeProgress.getOrDefault(bimoe, 0);
    }

    @Override
    public void setBimoeProgress(Bimoe bimoe, Integer value) {
        bimoeProgress.put(bimoe, value);
    }

    @Override
    public int getBimoeTextLength(Bimoe bimoe) {
        return bimoeTextLength.getOrDefault(bimoe, 0);
    }

    @Override
    public void setBimoeTextLength(Bimoe bimoe, Integer value) {
        bimoeTextLength.put(bimoe, value);
    }

    @Inject(at = @At("HEAD"), method = "writeCustomDataToTag")
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
        for (Map.Entry<Bimoe, Integer> entry : bimoeProgress.entrySet())
            tag.putInt(entry.getKey().name(), entry.getValue());
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromTag")
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
        for (Bimoe bimoe : Bimoe.values()) {
            final Integer value = tag.contains(bimoe.name()) ? tag.getInt(bimoe.name()) : 0;
            bimoeProgress.put(bimoe, value);
        }
    }
}
