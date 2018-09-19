package com.multipiston.coremod.configuration;

import net.minecraftforge.common.config.Config;
import static com.multipiston.coremod.Constants.MOD_ID;

@Config(modid = MOD_ID)
public class Configurations
{
    @Config.Comment("All configuration related to gameplay")
    public static Gameplay gameplay = new Gameplay();

    public static class Gameplay
    {
        @Config.Comment("Max multipiston range")
        public int max_range = 10;
     }
}
