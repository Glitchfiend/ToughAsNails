/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 *
 * All Rights Reserved.
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
        return IThirstHelper.impl.getThirst();
    }

    /**
     * Checks whether a player is able to drink.
     * @param player the player to check
     * @param ignoreThirst allow drinking regardless of the player's thirst level
     * @return whether the player can drink
     */
    public static boolean canDrink(PlayerEntity player, boolean ignoreThirst)
    {
        return IThirstHelper.impl.canDrink(player, ignoreThirst);
    }

    // Internal implementation details
    public interface IThirstHelper
    {
        IThirstHelper impl = null;

        IThirst getThirst();
        boolean canDrink(PlayerEntity player, boolean ignoreThirst);
    }
}
