package io.github.geeleonidas.stubborn.block

import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.StubbornBlock
import io.github.geeleonidas.stubborn.block.entity.TransceiverBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.EntityContext
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView


class TransceiverBlock : Block(Settings.of(Material.METAL)), BlockEntityProvider, StubbornBlock {
    override val id: Identifier = Stubborn.makeId("transceiver")
    init { register(this) }

    override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, context: EntityContext?): VoxelShape {
        val units = 1.0 / 16.0
        return VoxelShapes.cuboid(
            2 * units, 0 * units, 2 * units,
            14 * units, 15 * units, 14 * units
        )
    }

    override fun createBlockEntity(view: BlockView?): BlockEntity? {
        return TransceiverBlockEntity()
    }
}