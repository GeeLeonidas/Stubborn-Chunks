package io.github.geeleonidas.stubborn.mixin;

import io.github.geeleonidas.stubborn.Bimoe;
import io.github.geeleonidas.stubborn.util.StubbornPlayer;
import net.minecraft.entity.damage.DamageSource;
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
    private final HashMap<Bimoe, Integer> currentDialog = new HashMap<>();
    private final HashMap<Bimoe, Integer> currentEntry = new HashMap<>();

    private Integer deathCount = 0;

    @Override
    public int getBimoeProgress(Bimoe bimoe) {
        return bimoeProgress.getOrDefault(bimoe, 0);
    }

    @Override
    public void setBimoeProgress(Bimoe bimoe, Integer value) {
        bimoeProgress.put(bimoe, value);
    }

    @Override
    public int getCurrentDialog(Bimoe bimoe) {
        return currentDialog.getOrDefault(bimoe, -1);
    }

    @Override
    public void setCurrentDialog(Bimoe bimoe, Integer value) {
        currentDialog.put(bimoe, value);
    }

    @Override
    public int getCurrentEntry(Bimoe bimoe) {
        return currentEntry.getOrDefault(bimoe, 0);
    }

    @Override
    public void setCurrentEntry(Bimoe bimoe, Integer value) {
        currentEntry.put(bimoe, value);
    }

    @Override
    public int getBimoeTextLength(Bimoe bimoe) {
        return bimoeTextLength.getOrDefault(bimoe, 0);
    }

    @Override
    public void setBimoeTextLength(Bimoe bimoe, Integer value) {
        bimoeTextLength.put(bimoe, value);
    }

    @Override
    public int getDeathCount() {
        return deathCount;
    }

    @Inject(at = @At("HEAD"), method = "onDeath")
    public void onDeath(DamageSource source, CallbackInfo info) {
        deathCount++;
    }

    @Inject(at = @At("HEAD"), method = "writeCustomDataToTag")
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
        for (Map.Entry<Bimoe, Integer> entry : bimoeProgress.entrySet())
            tag.putInt(entry.getKey().name() + "_PROGRESS", entry.getValue());

        for (Map.Entry<Bimoe, Integer> entry : bimoeProgress.entrySet())
            tag.putInt(entry.getKey().name() + "_DIALOG", entry.getValue());

        for (Map.Entry<Bimoe, Integer> entry : bimoeProgress.entrySet())
            tag.putInt(entry.getKey().name() + "_ENTRY", entry.getValue());
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromTag")
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
        for (Bimoe bimoe : Bimoe.values()) {
            final String progressKey = bimoe.makeCompoundTagKey("Progress");
            final Integer progress = tag.contains(progressKey)? tag.getInt(progressKey) : 0;
            bimoeProgress.put(bimoe, progress);

            final String dialogKey = bimoe.makeCompoundTagKey("Dialog");
            final Integer dialog = tag.contains(dialogKey)? tag.getInt(dialogKey) : -1;
            currentDialog.put(bimoe, dialog);

            final String entryKey = bimoe.makeCompoundTagKey("Entry");
            final Integer entry = tag.contains(entryKey)? tag.getInt(entryKey) : 0;
            currentEntry.put(bimoe, entry);
        }
    }
}
