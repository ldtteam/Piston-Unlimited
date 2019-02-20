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
    public static MultiPiston andesite;
    public static MultiPiston diorite;
    public static MultiPiston granite;
    public static MultiPiston stone;
    
    public static MultiPiston oak;
    public static MultiPiston spruce;
    public static MultiPiston birch;
    public static MultiPiston acacia;
    public static MultiPiston darkOak;
    public static MultiPiston jungleWood;

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
        andesite = new MultiPiston("andesite").registerBlock(registry);
        diorite  = new MultiPiston("diorite").registerBlock(registry);
        granite  = new MultiPiston("granite").registerBlock(registry);
        stone  = new MultiPiston("stone").registerBlock(registry);
        
        oak  = new MultiPiston("oak").registerBlock(registry);
        spruce  = new MultiPiston("spruce").registerBlock(registry);
        birch  = new MultiPiston("birch").registerBlock(registry);
        acacia  = new MultiPiston("acacia").registerBlock(registry);
        darkOak  = new MultiPiston("darkOak").registerBlock(registry);
        jungleWood  = new MultiPiston("jungleWood").registerBlock(registry);
    }

    public static void registerItemBlock(final IForgeRegistry<Item> registry)
    {
        andesite.registerItemBlock(registry);
        diorite.registerItemBlock(registry);
        granite.registerItemBlock(registry);
        stone.registerItemBlock(registry);
        oak.registerItemBlock(registry);
        spruce.registerItemBlock(registry);
        birch.registerItemBlock(registry);
        acacia.registerItemBlock(registry);
        darkOak.registerItemBlock(registry);
        jungleWood.registerItemBlock(registry);
    }
}
