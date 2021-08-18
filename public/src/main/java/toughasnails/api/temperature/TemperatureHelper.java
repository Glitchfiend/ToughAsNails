/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;

public class TemperatureHelper
{
    /**
     * Check the temperature at a given position.
     * @param level the current level.
     * @param pos the position to check.
     * @return temperature level.
     */
    public static TemperatureLevel getTemperatureAtPos(Level level, BlockPos pos)
    {
        return Impl.INSTANCE.getTemperatureAtPos(level, pos);
    }

    public static TemperatureLevel getPlayerTemperature(Player player)
    {
        return Impl.INSTANCE.getPlayerTemperature(player);
    }

    /** Internal implementation details */
    public static class Impl
    {
        public static TemperatureHelper.Impl.ITemperatureHelper INSTANCE = null;

        public interface ITemperatureHelper
        {
            TemperatureLevel getTemperatureAtPos(Level level, BlockPos pos);
            TemperatureLevel getPlayerTemperature(Player player);
        }
    }
}
