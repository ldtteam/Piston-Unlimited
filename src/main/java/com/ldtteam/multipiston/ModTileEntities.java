package com.ldtteam.multipiston;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.ldtteam.multipiston.MultiPiston.MOD_ID;

public final class ModTileEntities
{
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MOD_ID);

    private ModTileEntities() { /* prevent construction */ }

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityMultiPiston>>
      multipiston = TILE_ENTITIES.register(
          "multipistonte",
          () -> new BlockEntityType<>(TileEntityMultiPiston::new, new Block[] {ModBlocks.multipiston.value()})
      );
}
