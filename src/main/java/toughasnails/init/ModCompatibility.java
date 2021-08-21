/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
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
