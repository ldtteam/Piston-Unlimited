package com.ldtteam.multipiston;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = MultiPiston.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MultiPistonConfig
{
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> MOVEABLE_ENTITY_BLOCKS;

    private static volatile Set<ResourceLocation> allowedEntityBlocks = Collections.emptySet();
    private static volatile ModConfig serverConfig;

    static
    {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("multipiston");
        MOVEABLE_ENTITY_BLOCKS = builder
            .comment("Additional block ids (e.g. \"minecraft:barrel\") for block entities the multipiston may move.")
            .defineList("moveableEntityBlocks", List.of(), MultiPistonConfig::isValidBlockId);
        builder.pop();

        SPEC = builder.build();
    }

    private MultiPistonConfig()
    {
    }

    public static void register()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SPEC);
    }

    public static boolean isAllowedEntityBlock(final ResourceLocation blockId)
    {
        return allowedEntityBlocks.contains(blockId);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading event)
    {
        if (isOurServerConfig(event.getConfig()))
        {
            serverConfig = event.getConfig();
            bake();
        }
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading event)
    {
        if (isOurServerConfig(event.getConfig()))
        {
            serverConfig = event.getConfig();
            bake();
        }
    }

    private static void bake()
    {
        final Set<ResourceLocation> updated = new HashSet<>();
        for (final String blockId : MOVEABLE_ENTITY_BLOCKS.get())
        {
            if (blockId == null || blockId.isBlank())
            {
                continue;
            }
            try
            {
                updated.add(new ResourceLocation(blockId.trim()));
            }
            catch (final RuntimeException ex)
            {
                MultiPiston.LOGGER.warn("Invalid multipiston moveableEntityBlocks entry: {}", blockId, ex);
            }
        }
        allowedEntityBlocks = Collections.unmodifiableSet(updated);
    }

    public static List<String> getMoveableEntityBlockIds()
    {
        final List<String> sanitized = sanitizeMoveableEntityBlocks(MOVEABLE_ENTITY_BLOCKS.get());
        return Collections.unmodifiableList(sanitized);
    }

    public static boolean addMoveableEntityBlock(final ResourceLocation blockId)
    {
        final String entry = blockId.toString();
        final List<String> updated = sanitizeMoveableEntityBlocks(MOVEABLE_ENTITY_BLOCKS.get());
        if (updated.contains(entry))
        {
            return false;
        }
        updated.add(entry);
        setMoveableEntityBlocks(updated);
        return true;
    }

    public static boolean removeMoveableEntityBlock(final ResourceLocation blockId)
    {
        final String entry = blockId.toString();
        final List<String> updated = sanitizeMoveableEntityBlocks(MOVEABLE_ENTITY_BLOCKS.get());
        final boolean removed = updated.removeIf(value -> value.equals(entry));
        if (removed)
        {
            setMoveableEntityBlocks(updated);
        }
        return removed;
    }

    private static List<String> sanitizeMoveableEntityBlocks(final List<? extends String> values)
    {
        final List<String> sanitized = new ArrayList<>();
        for (final String value : values)
        {
            if (value == null || value.isBlank())
            {
                continue;
            }
            sanitized.add(value.trim());
        }
        return sanitized;
    }

    private static void setMoveableEntityBlocks(final List<String> values)
    {
        final List<String> unique = new ArrayList<>(new LinkedHashSet<>(values));
        MOVEABLE_ENTITY_BLOCKS.set(List.copyOf(unique));
        bake();
        save();
    }

    private static void save()
    {
        if (serverConfig != null)
        {
            serverConfig.save();
        }
    }

    private static boolean isValidBlockId(final Object value)
    {
        if (!(value instanceof String))
        {
            return false;
        }
        try
        {
            new ResourceLocation(((String) value).trim());
            return true;
        }
        catch (final RuntimeException ex)
        {
            return false;
        }
    }

    private static boolean isOurServerConfig(final ModConfig config)
    {
        return config.getType() == ModConfig.Type.SERVER && MultiPiston.MOD_ID.equals(config.getModId());
    }
}
