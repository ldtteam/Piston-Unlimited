package com.ldtteam.multipiston;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.ldtteam.multipiston.MultiPiston.MOD_ID;

public class ModBlocks
{
    public static final TagKey<Block> MOVEABLE_ENTITY_BLOCKS =
        BlockTags.create(Identifier.fromNamespaceAndPath(MOD_ID, "moveable_entity_blocks"));

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    public static final DeferredBlock<MultiPistonBlock> multipiston =
        BLOCKS.registerBlock("multipistonblock", MultiPistonBlock::new, BlockBehaviour.Properties::of);
    public static final DeferredItem<BlockItem> multipistonItem = ITEMS.registerSimpleBlockItem(multipiston);

}
