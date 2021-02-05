/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.thirst;

import net.minecraft.entity.player.PlayerEntity;

public class ThirstHelper
{
    /**
     * Obtains the thirst data for a player.
     * @param player the player to obtain thirst data for
     * @return the player's thirst data
     */
    public static IThirst getThirst(PlayerEntity player)
    {
        return Impl.INSTANCE.getThirst(player);
    }

    /**
     * Checks whether a player is able to drink.
     * @param player the player to check
     * @param ignoreThirst allow drinking regardless of the player's thirst level
     * @return whether the player can drink
     */
    public static boolean canDrink(PlayerEntity player, boolean ignoreThirst)
    {
        return Impl.INSTANCE.canDrink(player, ignoreThirst);
    }

    /**
     * Checks whether thirst is enabled.
     * @return whether thirst is enabled.
     */
    public static boolean isThirstEnabled()
    {
        return Impl.INSTANCE.isThirstEnabled();
    }

    /** Internal implementation details */
    public static class Impl
    {
        public static IThirstHelper INSTANCE = null;

        public interface IThirstHelper
        {
            IThirst getThirst(PlayerEntity player);
            boolean canDrink(PlayerEntity player, boolean ignoreThirst);
            boolean isThirstEnabled();
        }
    }
}
