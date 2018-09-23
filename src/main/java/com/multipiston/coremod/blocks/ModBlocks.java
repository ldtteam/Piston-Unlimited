package com.multipiston.coremod.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class to create the modBlocks.
 * References to the blocks can be made here
 */
public final class ModBlocks
{
    public static MultiPiston multiPiston;

    /**
     * Private constructor to hide the implicit public one.
     */
    private ModBlocks()
    {
        /*
         * Intentionally left empty.
         */
    }

    /**
     * Initiates all the blocks. At the correct time.
     * @param registry the registry to register it wiht.
     */
    public static void init(final IForgeRegistry<Block> registry)
    {
        multiPiston = new MultiPiston().registerBlock(registry);
    }

    public static void registerItemBlock(final IForgeRegistry<Item> registry)
    {
        multiPiston.registerItemBlock(registry);
    }
}
