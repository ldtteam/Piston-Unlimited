package com.ldtteam.multipiston;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Locale;
import java.util.function.Supplier;

import static com.ldtteam.multipiston.MultiPiston.MOD_ID;

public class ModBlocks
{
    public static final TagKey<Block> MOVEABLE_ENTITY_BLOCKS = BlockTags.create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "moveable_entity_blocks"));

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items  ITEMS  = DeferredRegister.createItems(MOD_ID);

    public static final DeferredBlock<MultiPistonBlock> multipiston = register("multipistonblock", MultiPistonBlock::new);

    /**
     * Utility shorthand to register blocks using the deferred registry
     * @param name the registry name of the block
     * @param block a factory / constructor to create the block on demand
     * @param <B> the block subclass for the factory response
     * @return the block entry saved to the registry
     */
    public static <B extends Block> DeferredBlock<B> register(String name, Supplier<B> block)
    {
        DeferredBlock<B> registered = BLOCKS.register(name.toLowerCase(Locale.ENGLISH), block);
        ITEMS.register(name.toLowerCase(Locale.ENGLISH), () -> new BlockItem(registered.get(), new Item.Properties()));
        return registered;
    }
}
