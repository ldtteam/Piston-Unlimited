package com.multipiston.coremod.blocks;

import com.multipiston.coremod.blocks.interfaces.IBlockMultiPiston;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class AbstractBlockMultiPiston<B extends AbstractBlockMultiPiston<B>> extends Block implements IBlockMultiPiston<B>
{
    public AbstractBlockMultiPiston(final Material materialIn)
    {
        super(materialIn);
    }

    /**
     * Registery block at gameregistry.
     *
     * @param registry the registry to use.
     * @return the block itself.
     */
    @Override
    public B registerBlock(final IForgeRegistry<Block> registry)
    {
        registry.register(this);
        return (B) this;
    }
    /**
     * Registery block at gameregistry.
     *
     * @param registry the registry to use.
     */
    @Override
    public void registerItemBlock(final IForgeRegistry<Item> registry)
    {
        registry.register((new ItemBlock(this)).setRegistryName(this.getRegistryName()));
    }
}
