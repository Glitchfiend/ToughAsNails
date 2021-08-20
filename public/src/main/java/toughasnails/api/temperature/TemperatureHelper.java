/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

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

    /**
     * Get the temperature for a player.
     * @param player the player.
     * @return temperature level.
     */
    public static TemperatureLevel getTemperatureForPlayer(Player player)
    {
        return getTemperatureData(player).getLevel();
    }

    /**
     * Obtains the temperature data for a player.
     * @param player the player to obtain temperature data for
     * @return the player's temperature data
     */
    public static ITemperature getTemperatureData(Player player)
    {
        return Impl.INSTANCE.getPlayerTemperature(player);
    }

    /**
     * Checks whether temperature is enabled.
     * @return whether temperature is enabled.
     */
    public static boolean isTemperatureEnabled()
    {
        return Impl.INSTANCE.isTemperatureEnabled();
    }

    /** Internal implementation details */
    public static class Impl
    {
        public static TemperatureHelper.Impl.ITemperatureHelper INSTANCE = null;

        public interface ITemperatureHelper
        {
            TemperatureLevel getTemperatureAtPos(Level level, BlockPos pos);
            ITemperature getPlayerTemperature(Player player);
            boolean isTemperatureEnabled();
        }
    }
}
