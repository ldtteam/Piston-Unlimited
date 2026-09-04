package com.ldtteam.multipiston;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The configurable piston block.
 */
public final class MultiPistonBlock extends BaseEntityBlock
{
    public static final MapCodec<MultiPistonBlock> CODEC = simpleCodec(MultiPistonBlock::new);

    private static final float BLOCK_HARDNESS = 1.0F;
    private static final float RESISTANCE = 1.0F;
    private static final VoxelShape SHAPE = Block.box(0.01D, 0.01D, 0.01D, 15.99D, 15.99D, 15.99D);

    public MultiPistonBlock(final Properties properties)
    {
        super(properties.mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(BLOCK_HARDNESS, RESISTANCE)
            .isRedstoneConductor((state, level, pos) -> true));
    }

    @Override
    protected MapCodec<MultiPistonBlock> codec()
    {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(
        @NotNull final BlockState state,
        @NotNull final Level level,
        @NotNull final BlockPos pos,
        @NotNull final Player player,
        @NotNull final BlockHitResult hitResult
    )
    {
        if (level.isClientSide())
        {
            new WindowMultiPiston(pos).open();
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getCollisionShape(
        @NotNull final BlockState state,
        @NotNull final BlockGetter level,
        @NotNull final BlockPos pos,
        @NotNull final CollisionContext context
    )
    {
        return Shapes.block();
    }

    @Override
    protected void neighborChanged(
        @NotNull final BlockState state,
        @NotNull final Level level,
        @NotNull final BlockPos pos,
        @NotNull final Block block,
        @Nullable final Orientation orientation,
        final boolean movedByPiston
    )
    {
        if (level.isClientSide())
        {
            return;
        }

        if (level.getBlockEntity(pos) instanceof TileEntityMultiPiston piston)
        {
            piston.handleRedstone(level.hasNeighborSignal(pos));
        }
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull final BlockPos pos, @NotNull final BlockState state)
    {
        return new TileEntityMultiPiston(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        @NotNull final Level level,
        @NotNull final BlockState state,
        @NotNull final BlockEntityType<T> type
    )
    {
        return createTickerHelper(type, ModTileEntities.multipiston.value(), (tickLevel, pos, tickState, piston) -> piston.tick());
    }

    @Override
    @NotNull
    protected RenderShape getRenderShape(@NotNull final BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    @NotNull
    protected VoxelShape getShape(
        @NotNull final BlockState state,
        @NotNull final BlockGetter level,
        @NotNull final BlockPos pos,
        @NotNull final CollisionContext context
    )
    {
        return SHAPE;
    }
}
