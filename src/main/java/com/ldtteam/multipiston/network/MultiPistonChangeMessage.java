package com.ldtteam.multipiston.network;

import com.ldtteam.common.network.AbstractServerPlayMessage;
import com.ldtteam.common.network.PlayMessageType;
import com.ldtteam.multipiston.TileEntityMultiPiston;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import static com.ldtteam.multipiston.MultiPiston.MOD_ID;

/**
 * Message class which handles updating the minecolonies multipiston.
 */
public class MultiPistonChangeMessage extends AbstractServerPlayMessage
{
    public static final PlayMessageType<?> TYPE = PlayMessageType.forServer(MOD_ID, "multi_piston_change", MultiPistonChangeMessage::new);

    /**
     * The direction it should push or pull rom.
     */
    private Direction input;

    /**
     * The direction it should push or pull rom.
     */
    private Direction output;

    /**
     * The range it should pull to.
     */
    private int range;

    /**
     * The speed it should have.
     */
    private int speed;

    /**
     * The position of the tileEntity.
     */
    private BlockPos pos;

    /**
     * Empty public constructor.
     */
    public MultiPistonChangeMessage(final FriendlyByteBuf buf, final PlayMessageType<?> type)
    {
        super(buf, type);
        this.pos = buf.readBlockPos();
        this.input = Direction.values()[buf.readInt()];
        this.output = Direction.values()[buf.readInt()];
        this.range = buf.readInt();
        this.speed = buf.readInt();
    }

    /**
     * Constructor to create the 
     * @param pos the position of the block.
     * @param input the way it inputs from.
     * @param output the way it will output to.
     * @param range the range it should work.
     * @param speed the speed it should have.
     */
    public MultiPistonChangeMessage(final BlockPos pos, final Direction input, final Direction output, final int range, final int speed)
    {
        super(TYPE);
        this.pos = pos;
        this.input = input;
        this.range = range;
        this.output = output;
        this.speed = speed;
    }

    @Override
    public void toBytes(final FriendlyByteBuf buf)
    {
        buf.writeBlockPos(pos);
        buf.writeInt(input.ordinal());
        buf.writeInt(output.ordinal());
        buf.writeInt(range);
        buf.writeInt(speed);
    }

    @Override
    protected void onExecute(final PlayPayloadContext context, final ServerPlayer player)
    {
        final Level world = player.level();
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof TileEntityMultiPiston multiPiston)
        {
            multiPiston.setInput(input);
            multiPiston.setOutput(output);
            multiPiston.setRange(range);
            multiPiston.setSpeed(speed);
            final BlockState state = world.getBlockState(pos);
            world.sendBlockUpdated(pos, state, state, 0x3);
        }
    }
}
