package io.github.geeleonidas.stubborn.block.entity

import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.StubbornInit
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Tickable

class TransceiverBlockEntity : BlockEntity(StubbornInit.transceiverBlockEntityType), BlockEntityClientSerializable, Tickable {
    var chunkRadius = 0.toByte()

    override fun tick() {
        if (world == null)
            return
        else if (world!!.isClient)
            return

        for (i in 0..chunkRadius) {
            if (world!!.time % 60L == 0L)
                Stubborn.log("$i")
        }
    }

    override fun toTag(tag: CompoundTag?): CompoundTag {
        tag!!.putByte("Radius", chunkRadius)
        return super.toTag(tag)
    }
    override fun fromTag(tag: CompoundTag?) {
        super.fromTag(tag)
        chunkRadius = tag!!.getByte("Radius")
    }

    override fun toClientTag(tag: CompoundTag?): CompoundTag {
        tag!!.putByte("Radius", chunkRadius)
        return super.toTag(tag)
    }
    override fun fromClientTag(tag: CompoundTag?) {
        super.fromTag(tag)
        chunkRadius = tag!!.getByte("Radius")
    }
}