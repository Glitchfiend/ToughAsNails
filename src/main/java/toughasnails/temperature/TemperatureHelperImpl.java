/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import toughasnails.api.temperature.IPositionalTemperatureModifier;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.core.ToughAsNails;

import java.util.List;

public class TemperatureHelperImpl implements TemperatureHelper.Impl.ITemperatureHelper
{
    // TODO: Offset by nearby heat sources
    // TODO: Offset by weather

    private static List<IPositionalTemperatureModifier> positionalModifiers = Lists.newArrayList(TemperatureHelperImpl::nightModifier);

    @Override
    public TemperatureLevel getTemperatureAtPos(Level level, BlockPos pos)
    {
        Biome biome = level.getBiome(pos);
        TemperatureLevel temperature = getBiomeTemperatureLevel(biome, pos);

        for (IPositionalTemperatureModifier modifier : positionalModifiers)
        {
            temperature = modifier.modify(level, pos, temperature);
        }

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

    private static TemperatureLevel getBiomeTemperatureLevel(Biome biome, BlockPos pos)
    {
        float biomeTemperature = biome.getTemperature(pos);

        if (biomeTemperature < 0.15F) return TemperatureLevel.ICY;
        else if (biomeTemperature >= 0.15F && biomeTemperature < 0.5F) return TemperatureLevel.COLD;
        else if (biomeTemperature >= 0.5F && biomeTemperature < 0.7F) return TemperatureLevel.NEUTRAL;
        else if (biomeTemperature >= 0.7F && biomeTemperature < 0.9F) return TemperatureLevel.WARM;
        else if (biomeTemperature > 0.9F) return TemperatureLevel.HOT;

        return TemperatureLevel.NEUTRAL;
    }

    private static TemperatureLevel nightModifier(Level level, BlockPos pos, TemperatureLevel current)
    {
        // Drop the temperature during the night
        if (level.getDayTime() % 24000L > 13000L)
        {
            if (current == TemperatureLevel.HOT)
                current = current.decrement(2);
            else if (current != TemperatureLevel.NEUTRAL)
                current = current.decrement(1);
        }

        return current;
    }


}
