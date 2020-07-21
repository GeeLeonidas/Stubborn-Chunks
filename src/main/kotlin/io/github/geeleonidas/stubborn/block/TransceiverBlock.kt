package io.github.geeleonidas.stubborn.block

import io.github.geeleonidas.stubborn.Stubborn
import io.github.geeleonidas.stubborn.StubbornBlock
import io.github.geeleonidas.stubborn.block.entity.TransceiverBlockEntity
import net.minecraft.block.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class TransceiverBlock:
    HorizontalFacingBlock(Settings.of(Material.METAL)),
    BlockEntityProvider,
    StubbornBlock {

    override val id: Identifier = Stubborn.makeId("transceiver")
    init {
        register(this)
        defaultState = stateManager.defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(Properties.HORIZONTAL_FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState =
        defaultState.with(FACING, ctx?.playerFacing?.opposite)

    override fun onUse(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity?,
                       hand: Hand?, hit: BlockHitResult?): ActionResult {
        player?.openHandledScreen(state?.createScreenHandlerFactory(world, pos))
        return ActionResult.SUCCESS
    }

    override fun getOutlineShape(state: BlockState?, world: BlockView?,
                                 pos: BlockPos?, context: ShapeContext?): VoxelShape {
        val units = 1.0 / 16.0
        return VoxelShapes.cuboid(
            2 * units, 0 * units, 2 * units,
            14 * units, 14.5 * units, 14 * units
        )
    }

    override fun createScreenHandlerFactory(state: BlockState?, world: World?, pos: BlockPos?) =
        world?.getBlockEntity(pos) as TransceiverBlockEntity?

    override fun createBlockEntity(blockView: BlockView?) =
        TransceiverBlockEntity()

}