/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.thirst;

import net.minecraft.entity.player.PlayerEntity;
import toughasnails.api.capability.TANCapabilities;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.config.ServerConfig;

public class ThirstHelperImpl implements ThirstHelper.Impl.IThirstHelper
{
    private IThirst lastThirst;

    @Override
    public IThirst getThirst(PlayerEntity player)
    {
        IThirst thirst = player.getCapability(TANCapabilities.THIRST).orElse(lastThirst);
        lastThirst = thirst;
        return thirst;
    }

    @Override
    public boolean canDrink(PlayerEntity player, boolean ignoreThirst)
    {
        IThirst thirst = getThirst(player);
        return (player.abilities.invulnerable || ignoreThirst || thirst.isThirsty()) && isThirstEnabled();
    }

    @Override
    public boolean isThirstEnabled()
    {
        return ServerConfig.enableThirst.get();
    }
}

