/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
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

