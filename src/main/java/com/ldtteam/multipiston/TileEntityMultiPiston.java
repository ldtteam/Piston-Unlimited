package com.ldtteam.multipiston;

import com.google.common.primitives.Ints;
import com.ldtteam.structurize.api.util.IRotatableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.UP;

/**
 * The multi-piston block entity, including its persisted movement configuration.
 */
public class TileEntityMultiPiston extends BlockEntity implements IRotatableBlockEntity
{
    public static final String TAG_INPUT = "input";
    public static final String TAG_RANGE = "range";
    public static final String TAG_DIRECTION = "direction";
    public static final String TAG_LENGTH = "length";
    public static final String TAG_PROGRESS = "progress";
    public static final String TAG_OUTPUT_DIRECTION = "outputDirection";
    public static final String TAG_SPEED = "speed";

    private static final double VOLUME = 0.5D;
    private static final double PITCH = 0.8D;
    private static final int MAX_RANGE = 10;
    private static final int MAX_SPEED = 3;
    private static final int MIN_SPEED = 1;
    public static final int DEFAULT_RANGE = 3;
    public static final int DEFAULT_SPEED = 2;

    private boolean on;
    private Direction input = UP;
    private Direction output = DOWN;
    private int range = DEFAULT_RANGE;
    private Direction currentDirection;
    private int progress;
    private int ticksPassed;
    private int speed = DEFAULT_SPEED;

    public TileEntityMultiPiston(final BlockPos pos, final BlockState state)
    {
        super(ModTileEntities.multipiston.value(), pos, state);
    }

    public void handleRedstone(final boolean signal)
    {
        if (signal != on && progress == range)
        {
            on = signal;
            currentDirection = signal ? output : input;
            progress = 0;
        }
    }

    public void tick()
    {
        if (level == null || level.isClientSide())
        {
            return;
        }

        if (currentDirection == null && progress < range)
        {
            progress = range;
        }

        if (progress < range && ticksPassed % (20 / speed) == 0)
        {
            handleTick();
            ticksPassed = 1;
        }
        else if (progress < range)
        {
            ticksPassed++;
        }
    }

    private void handleTick()
    {
        if (level == null || currentDirection == null)
        {
            return;
        }

        final Direction currentOutputDirection = currentDirection == input ? output : input;
        if (progress >= range)
        {
            return;
        }

        final BlockState blockToMove = level.getBlockState(worldPosition.relative(currentDirection, 1));
        final boolean blocked = blockToMove.getBlock() == Blocks.AIR
            || blockToMove.getPistonPushReaction() == PushReaction.IGNORE
            || blockToMove.getPistonPushReaction() == PushReaction.DESTROY
            || blockToMove.getPistonPushReaction() == PushReaction.BLOCK
            || blockToMove.getBlock() == Blocks.BEDROCK
            || (blockToMove.getBlock() instanceof EntityBlock
                && !"domum_ornamentum".equals(BuiltInRegistries.BLOCK.getKey(blockToMove.getBlock()).getNamespace())
                && !blockToMove.is(ModBlocks.MOVEABLE_ENTITY_BLOCKS));
        if (blocked)
        {
            progress++;
            return;
        }

        for (int i = 0; i < Math.min(range, MAX_RANGE); i++)
        {
            final int blockToGoTo = i - 1 - progress + (i - 1 - progress >= 0 ? 1 : 0);
            final int blockToGoFrom = i + 1 - progress - (i + 1 - progress <= 0 ? 1 : 0);
            final BlockPos posToGo = blockToGoTo > 0
                ? worldPosition.relative(currentDirection, blockToGoTo)
                : worldPosition.relative(currentOutputDirection, Math.abs(blockToGoTo));
            final BlockPos posToGoFrom = blockToGoFrom > 0
                ? worldPosition.relative(currentDirection, blockToGoFrom)
                : worldPosition.relative(currentOutputDirection, Math.abs(blockToGoFrom));

            if ((!level.isEmptyBlock(posToGo) && !level.getBlockState(posToGo).liquid())
                || level.getBlockState(posToGoFrom).getBlock() != blockToMove.getBlock()
                || !level.hasChunkAt(posToGoFrom)
                || !level.hasChunkAt(posToGo))
            {
                continue;
            }

            pushEntitiesIfNecessary(posToGo, worldPosition);
            BlockState movingState = level.getBlockState(posToGoFrom);
            movingState = Block.updateFromNeighbourShapes(movingState, level, posToGo);
            level.setBlock(posToGo, movingState, 67);

            if (movingState.getBlock() instanceof BucketPickup bucketPickup)
            {
                bucketPickup.pickupBlock(null, level, posToGo, movingState);
            }
            level.neighborChanged(posToGo, movingState.getBlock(), null);

            if (movingState.getBlock() instanceof EntityBlock)
            {
                copyBlockEntity(level.getBlockEntity(posToGoFrom), level.getBlockEntity(posToGo));
            }

            level.removeBlockEntity(posToGoFrom);
            level.removeBlock(posToGoFrom, true);
        }

        level.playSound(null, worldPosition, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, (float) VOLUME, (float) PITCH);
        progress++;
    }

    private static void copyBlockEntity(final BlockEntity source, final BlockEntity target)
    {
        if (source == null || target == null || source.getLevel() == null)
        {
            return;
        }

        final TagValueOutput output = TagValueOutput.createWithContext(
            ProblemReporter.DISCARDING,
            source.getLevel().registryAccess()
        );
        source.saveWithFullMetadata(output);
        target.loadWithComponents(TagValueInput.create(
            ProblemReporter.DISCARDING,
            source.getLevel().registryAccess(),
            output.buildResult()
        ));
        target.setChanged();
    }

    private void pushEntitiesIfNecessary(final BlockPos posToGo, final BlockPos sourcePos)
    {
        if (level == null)
        {
            return;
        }

        final List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(posToGo));
        final BlockPos vector = posToGo.subtract(sourcePos);
        final BlockPos posTo = posToGo.relative(getNearest(vector.getX(), vector.getY(), vector.getZ()));
        for (final Entity entity : entities)
        {
            entity.teleportTo(posTo.getX() + 0.5D, posTo.getY() + 0.5D, posTo.getZ() + 0.5D);
        }
    }

    private static Direction getNearest(final double x, final double y, final double z)
    {
        if (Math.abs(y) >= Math.abs(x) && Math.abs(y) >= Math.abs(z))
        {
            return y < 0.0D ? DOWN : UP;
        }
        if (Math.abs(x) >= Math.abs(z))
        {
            return x < 0.0D ? Direction.WEST : Direction.EAST;
        }
        return z < 0.0D ? Direction.NORTH : Direction.SOUTH;
    }

    @Override
    public void rotate(@NotNull final Rotation rotation)
    {
        if (!isVertical(output))
        {
            output = rotation.rotate(output);
        }
        if (!isVertical(input))
        {
            input = rotation.rotate(input);
        }
    }

    @Override
    public void mirror(@NotNull final Mirror mirror)
    {
        if (!isVertical(output))
        {
            output = mirror.mirror(output);
        }
        if (!isVertical(input))
        {
            input = mirror.mirror(input);
        }
    }

    private static boolean isVertical(final Direction direction)
    {
        return direction == UP || direction == DOWN;
    }

    public boolean isOn()
    {
        return on;
    }

    public Direction getInput()
    {
        return input;
    }

    public Direction getOutput()
    {
        return output;
    }

    public void setInput(final Direction direction)
    {
        this.input = direction;
    }

    public void setOutput(final Direction output)
    {
        this.output = output;
    }

    public int getRange()
    {
        return range;
    }

    public void setRange(final int requestedRange)
    {
        this.range = Math.min(requestedRange, MAX_RANGE);
        this.progress = range;
    }

    public int getSpeed()
    {
        return speed;
    }

    public void setSpeed(final int requestedSpeed)
    {
        this.speed = Ints.constrainToRange(requestedSpeed, MIN_SPEED, MAX_SPEED);
    }

    @Override
    protected void loadAdditional(@NotNull final ValueInput input)
    {
        super.loadAdditional(input);
        range = input.getIntOr(TAG_RANGE, DEFAULT_RANGE);
        progress = input.getIntOr(TAG_PROGRESS, 0);
        final int inputOrdinal = input.getIntOr(TAG_DIRECTION, UP.ordinal());
        this.input = Direction.values()[Math.floorMod(inputOrdinal, Direction.values().length)];
        on = input.getBooleanOr(TAG_INPUT, false);
        output = input.getInt(TAG_OUTPUT_DIRECTION)
            .map(index -> Direction.values()[Math.floorMod(index, Direction.values().length)])
            .orElse(this.input.getOpposite());
        speed = Math.max(MIN_SPEED, input.getIntOr(TAG_SPEED, DEFAULT_SPEED));
    }

    @Override
    protected void saveAdditional(@NotNull final ValueOutput output)
    {
        super.saveAdditional(output);
        output.putInt(TAG_RANGE, range);
        output.putInt(TAG_PROGRESS, progress);
        output.putInt(TAG_DIRECTION, input.ordinal());
        output.putBoolean(TAG_INPUT, on);
        output.putInt(TAG_OUTPUT_DIRECTION, this.output.ordinal());
        output.putInt(TAG_SPEED, speed);
    }

    @Override
    public CompoundTag getUpdateTag(final HolderLookup.Provider registries)
    {
        return saveWithFullMetadata(registries);
    }

    @Override
    public void handleUpdateTag(final ValueInput input)
    {
        loadAdditional(input);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(final Connection connection, final ValueInput input)
    {
        loadAdditional(input);
    }
}
