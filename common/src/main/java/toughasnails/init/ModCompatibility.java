/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import toughasnails.glitch.util.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.init.ModTags;
import sereneseasons.season.SeasonHooks;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.core.ToughAsNails;

public class ModCompatibility
{
    public static void init()
    {
        if (Environment.isModLoaded("sereneseasons"))
        {
            ToughAsNails.LOGGER.info("Serene Seasons detected. Enabling season modifier.");
            TemperatureHelper.registerPositionalTemperatureModifier(ModCompatibility::seasonModifier);
        }
    }

    public static boolean coldEnoughToSnowSeasonal(Level level, Holder<Biome> biome, BlockPos pos)
    {
        return SeasonHooks.coldEnoughToSnowSeasonal(level, biome, pos);
    }

    private static TemperatureLevel seasonModifier(Level level, BlockPos pos, TemperatureLevel current)
    {
        Holder<Biome> biome = level.getBiome(pos);

        // Only adjust if above the environmental modifier altitude
        if (level.dimensionType().natural() && pos.getY() <= ModConfig.temperature.environmentalModifierAltitude && pos.getY() < level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).below().getY())
            return current;

        // Check if biome uses seasonal effects
        if (biome.is(ModTags.Biomes.BLACKLISTED_BIOMES))
            return current;

        Season.TropicalSeason tropicalSeason = SeasonHelper.getSeasonState(level).getTropicalSeason();

        // Adjust for mid dry/wet season
        if (biome.is(ModTags.Biomes.TROPICAL_BIOMES))
        {
            switch (tropicalSeason)
            {
                case MID_DRY:
                    current = current.increment(1);
                    break;

                case MID_WET:
                    if (current == TemperatureLevel.HOT)
                        current = current.decrement(1);
                    break;
            }

            return current;
        }

        Season season = SeasonHelper.getSeasonState(level).getSeason();

        switch (season)
        {
            case SUMMER:
                current = current.increment(1);
                break;

            case WINTER:
                current = current.decrement(1);
                break;
        }

        return current;
    }
}
