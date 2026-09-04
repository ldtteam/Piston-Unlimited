package com.ldtteam.multipiston;

import com.ldtteam.multipiston.network.MultiPistonChangeMessage;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.ldtteam.multipiston.ModBlocks.BLOCKS;
import static com.ldtteam.multipiston.ModBlocks.ITEMS;
import static com.ldtteam.multipiston.ModTileEntities.TILE_ENTITIES;

@Mod(MultiPiston.MOD_ID)
public final class MultiPiston
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "multipiston";

    public static final DeferredRegister<CreativeModeTab> TAB_REG =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GENERAL = TAB_REG.register(
        "general",
        () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModBlocks.multipiston.get()))
            .title(Component.translatable("block.multipiston.multipistonblock"))
            .displayItems((config, output) -> output.accept(ModBlocks.multipiston.get()))
            .build()
    );

    public MultiPiston(final FMLModContainer modContainer, final Dist dist)
    {
        final IEventBus modBus = modContainer.getEventBus();
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        TILE_ENTITIES.register(modBus);
        TAB_REG.register(modBus);
        modBus.register(MultiPiston.class);
    }

    @SubscribeEvent
    public static void onRegisterPayloads(final RegisterPayloadHandlersEvent event)
    {
        final PayloadRegistrar registrar = event.registrar(MOD_ID).versioned("1.0");
        registrar.playToServer(
            MultiPistonChangeMessage.ID,
            MultiPistonChangeMessage.CODEC,
            MultiPistonChangeMessage::onExecute
        );
    }
}
