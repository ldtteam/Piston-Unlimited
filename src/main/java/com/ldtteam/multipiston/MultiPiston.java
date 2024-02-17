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
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.ldtteam.multipiston.ModBlocks.BLOCKS;
import static com.ldtteam.multipiston.ModBlocks.ITEMS;
import static com.ldtteam.multipiston.ModTileEntities.BLOCK_ENTITIES;
import static com.ldtteam.multipiston.MultiPiston.MOD_ID;

@Mod(MOD_ID)
public class MultiPiston
{
    public static final String                            MOD_ID  = "multipiston";
    public static final DeferredRegister<CreativeModeTab> TAB_REG = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final Supplier<CreativeModeTab>
      GENERAL = TAB_REG.register("general", () -> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 1).icon(() -> new ItemStack(ModBlocks.multipiston.get())).title(Component.translatable("itemGroup." + MOD_ID)).displayItems((config, output) -> {
        output.accept(ModBlocks.multipiston.get());
    }).build());

    public MultiPiston(final FMLModContainer modContainer, final Dist dist)
    {
        final IEventBus modBus = modContainer.getEventBus();

        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
        TAB_REG.register(modBus);

        modBus.register(this.getClass());
    }

    @SubscribeEvent
    public static void onNetworkRegistry(final RegisterPayloadHandlerEvent event)
    {
        final String modVersion = ModList.get().getModContainerById(MOD_ID).get().getModInfo().getVersion().toString();
        final IPayloadRegistrar registry = event.registrar(MOD_ID).versioned(modVersion);

        MultiPistonChangeMessage.TYPE.register(registry);
    }
}
