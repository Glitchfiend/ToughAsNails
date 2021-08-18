/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

public class TemperatureHelperImpl implements TemperatureHelper.Impl.ITemperatureHelper
{
    @Override
    public TemperatureLevel getTemperatureAtPos(Level level, BlockPos pos)
    {
        TemperatureLevel temperature = TemperatureLevel.NEUTRAL;
        Biome biome = level.getBiome(pos);
        float biomeTemperature = biome.getTemperature(pos);

        // Set the base temperature level based on the biome
        if (biomeTemperature < 0.15F) temperature = TemperatureLevel.ICY;
        else if (biomeTemperature >= 0.15F && biomeTemperature < 0.5F) temperature = TemperatureLevel.COLD;
        else if (biomeTemperature >= 0.5F && biomeTemperature < 0.7F) temperature = TemperatureLevel.NEUTRAL;
        else if (biomeTemperature >= 0.7F && biomeTemperature < 0.9F) temperature = TemperatureLevel.WARM;
        else if (biomeTemperature > 0.9F) temperature = TemperatureLevel.HOT;

        // TODO: Drop the temperature by one during the night
        // TODO: Offset by nearby heat sources
        // TODO: Offset by weather

        return temperature;
    }

    @Override
    public TemperatureLevel getPlayerTemperature(Player player)
    {
        TemperatureLevel temperature = getTemperatureAtPos(player.level, new BlockPos(player.position()));

        // TODO: Offset by clothing
        // TODO: Offset by in water
        // TODO: Offset when on fire
        // TODO: Potion effects
        // TODO: Armor enchantments

        return temperature;
    }
}
