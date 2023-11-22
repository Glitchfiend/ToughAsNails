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
     * Sets the number of ticks a player is experiencing hyperthermia.
     * @param player the player.
     * @param ticks number of ticks.
     */
    public static void setTicksHyperthermic(Player player, int ticks)
    {
        Impl.INSTANCE.setTicksHyperthermic(player, ticks);
    }

    /**
     * Get the percentage at which the player is hyperthermic.
     * @param player the player.
     * @return percentage hyperthermic.
     */
    public static float getPercentHyperthermic(Player player)
    {
        return Impl.INSTANCE.getPercentHyperthermic(player);
    }

    /**
     * Check whether the player is fully hyperthermic
     * @param player the player.
     * @return fully hyperthermic.
     */
    public static boolean isFullyHyperthermic(Player player)
    {
        return Impl.INSTANCE.isFullyHyperthermic(player);
    }

    /**
     * Gets the number of ticks required to experience hyperthermia.
     * @return The number of ticks required for hyperthermia.
     */
    public static int getTicksRequiredForHyperthermia()
    {
        return Impl.INSTANCE.getTicksRequiredForHyperthermia();
    }

    /**
     * Gets the ticks at which the player is hyperthermic.
     * @param player the player.
     * @return number of ticks.
     */
    public static int getTicksHyperthermic(Player player)
    {
        return Impl.INSTANCE.getTicksHyperthermic(player);
    }

    /**
     * Register a player temperature modifier.
     * @param modifier the modifier.
     */
    public static void registerPlayerTemperatureModifier(IPlayerTemperatureModifier modifier)
    {
        Impl.INSTANCE.registerPlayerTemperatureModifier(modifier);
    }

    /**
     * Register a positional temperature modifier.
     * @param modifier the modifier.
     */
    public static void registerPositionalTemperatureModifier(IPositionalTemperatureModifier modifier)
    {
        Impl.INSTANCE.registerPositionalTemperatureModifier(modifier);
    }

    /**
     * Register a proximity temperature modifier.
     * @param modifier the modifier.
     */
    public static void registerProximityBlockModifier(IProximityBlockModifier modifier)
    {
        Impl.INSTANCE.registerProximityBlockModifier(modifier);
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
            void setTicksHyperthermic(Player player, int ticks);
            float getPercentHyperthermic(Player player);
            boolean isFullyHyperthermic(Player player);
            int getTicksRequiredForHyperthermia();
            int getTicksHyperthermic(Player player);
            void registerPlayerTemperatureModifier(IPlayerTemperatureModifier modifier);
            void registerPositionalTemperatureModifier(IPositionalTemperatureModifier modifier);
            void registerProximityBlockModifier(IProximityBlockModifier modifier);
            boolean isTemperatureEnabled();
        }
    }
}
