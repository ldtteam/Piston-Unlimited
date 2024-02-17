package com.ldtteam.multipiston;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.ldtteam.multipiston.MultiPiston.MOD_ID;

public final class ModTileEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);

    private ModTileEntities() { /* prevent construction */ }

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityMultiPiston>> multipiston = BLOCK_ENTITIES.register("multipistonte",
      () -> BlockEntityType.Builder.of(TileEntityMultiPiston::new, ModBlocks.multipiston.get()).build(null));
}
