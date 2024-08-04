package com.ldtteam.multipiston;

import com.ldtteam.multipiston.network.MultiPistonChangeMessage;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

import static com.ldtteam.multipiston.ModBlocks.BLOCKS;
import static com.ldtteam.multipiston.ModBlocks.ITEMS;
import static com.ldtteam.multipiston.ModTileEntities.TILE_ENTITIES;

@Mod("multipiston")
public class MultiPiston
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String                            MOD_ID  = "multipiston";
    public static final DeferredRegister<CreativeModeTab> TAB_REG = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final Supplier<CreativeModeTab> GENERAL = TAB_REG.register("general", () -> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 1).icon(() -> new ItemStack(ModBlocks.multipiston.get())).title(Component.translatable("block.multipiston.multipistonblock")).displayItems((config, output) -> {
        output.accept(ModBlocks.multipiston.get());
    }).build());

    public MultiPiston(final FMLModContainer modContainer, final Dist dist)
    {
        final IEventBus modBus = modContainer.getEventBus();
        final IEventBus forgeBus = NeoForge.EVENT_BUS;
        
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        TILE_ENTITIES.register(modBus);
        TAB_REG.register(modBus);

        modBus.register(this.getClass());
    }

    @SubscribeEvent
    public static void onNetworkRegistry(final RegisterPayloadHandlersEvent event)
    {
        final String modVersion = ModList.get().getModContainerById(MOD_ID).get().getModInfo().getVersion().toString();
        final PayloadRegistrar registry = event.registrar(MOD_ID).versioned(modVersion);

        MultiPistonChangeMessage.TYPE.register(registry);
    }

}
