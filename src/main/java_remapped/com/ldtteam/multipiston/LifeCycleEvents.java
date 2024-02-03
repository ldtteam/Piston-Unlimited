package com.ldtteam.multipiston;

import com.ldtteam.multipiston.network.Network;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public class LifeCycleEvents
{
    @SubscribeEvent
    public static void onModInit(final FMLCommonSetupEvent event)
    {
        Network.getNetwork().registerCommonMessages();
    }
}
