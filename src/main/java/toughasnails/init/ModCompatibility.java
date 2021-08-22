/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fml.ModList;
import sereneseasons.config.BiomeConfig;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import toughasnails.config.TemperatureConfig;
import toughasnails.core.ToughAsNails;

public class ModCompatibility
{
    public static void init()
    {
        if (ModList.get().isLoaded("sereneseasons"))
        {
            ToughAsNails.logger.info("Serene Seasons detected. Enabling season modifier.");
            TemperatureHelper.registerPositionalTemperatureModifier(ModCompatibility::seasonModifier);
        }
    }

    private static TemperatureLevel seasonModifier(Level level, BlockPos pos, TemperatureLevel current)
    {
        ResourceKey<Biome> biomeKey = level.getBiomeName(pos).orElse(null);

        // Only adjust if above the environmental modifier altitude
        if (pos.getY() <= TemperatureConfig.environmentalModifierAltitude.get() && !level.canSeeSky(pos))
            return current;

        //Check if biome uses seasonal effects
        if (!BiomeConfig.enablesSeasonalEffects(biomeKey))
            return current;

        // Don't adjust the season if tropical seasons are in use
        if (BiomeConfig.usesTropicalSeasons(biomeKey))
            return current;

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
