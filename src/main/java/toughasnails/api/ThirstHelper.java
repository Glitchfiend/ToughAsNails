/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api;

import net.minecraft.entity.player.PlayerEntity;
import toughasnails.api.capability.IThirst;

public class ThirstHelper
{
    public static IThirst getThirst(PlayerEntity player)
    {
        return (IThirst)player.getCapability(TANCapabilities.THIRST).orElseThrow(NullPointerException::new);
    }
}
