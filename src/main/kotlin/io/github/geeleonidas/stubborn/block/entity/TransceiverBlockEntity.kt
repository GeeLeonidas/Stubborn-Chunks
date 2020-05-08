package io.github.geeleonidas.stubborn.block.entity

import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.StubbornInit
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Tickable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos

class TransceiverBlockEntity: BlockEntity(StubbornInit.transceiverBlockEntityType), BlockEntityClientSerializable, Tickable {
    var chunkRadius = 0.toByte()

    override fun markRemoved() {
        super.markRemoved()

        val w = world ?: return
        if (w.isClient) return

        Stubborn.log("Banned.")
    }

    override fun tick() {
        val w = world ?: return
        if (w.isClient) return

        for (x in -chunkRadius..chunkRadius)
            for (z in -chunkRadius..chunkRadius)
                setChunkForced(w as ServerWorld, pos, x, z)
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

private fun setChunkForced(serverWorld: ServerWorld, pos: BlockPos, offsetX: Int = 0, offsetZ: Int = 0, value: Boolean = true) {
    val centerChunk = ChunkPos(pos)
    serverWorld.setChunkForced(centerChunk.x + offsetX, centerChunk.z + offsetZ, value)
}