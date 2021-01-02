/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.thirst;

import net.minecraft.entity.player.PlayerEntity;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.capability.TANCapabilities;

public class ThirstHelper
{
    /**
     * Obtains the thirst data for a player.
     * @param player the player to obtain thirst data for
     * @return the player's thirst data
     */
    public static IThirst getThirst(PlayerEntity player)
    {
        return player.getCapability(TANCapabilities.THIRST).orElseThrow(NullPointerException::new);
    }

    /**
     * Checks whether a player is able to drink.
     * @param player the player to check
     * @param ignoreThirst allow drinking regardless of the player's thirst level
     * @return whether the player can drink
     */
    public static boolean canDrink(PlayerEntity player, boolean ignoreThirst)
    {
        IThirst thirst = getThirst(player);
        return player.abilities.invulnerable || ignoreThirst || thirst.isThirsty();
    }
}
