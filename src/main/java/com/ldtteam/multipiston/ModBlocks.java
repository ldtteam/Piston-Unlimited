package com.ldtteam.multipiston;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.ldtteam.multipiston.MultiPiston.MOD_ID;

public class ModBlocks
{
    public static final DeferredRegister.Blocks    BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items    ITEMS = DeferredRegister.createItems(MOD_ID);

    public static final DeferredBlock<MultiPistonBlock> multipiston = registerWithBlockItem("multipistonblock", MultiPistonBlock::new);

    /**
     * Utility shorthand to register blocks using the deferred registry
     * @param name the registry name of the block
     * @param block a factory / constructor to create the block on demand
     * @param <B> the block subclass for the factory response
     * @return the block entry saved to the registry
     */
    public static <B extends Block> DeferredBlock<B> registerWithBlockItem(String name, Supplier<B> block)
    {
        final DeferredBlock<B> registered = BLOCKS.register(name, block);
        ITEMS.registerSimpleBlockItem(registered);
        return registered;
    }
}
