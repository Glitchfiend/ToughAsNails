/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

import net.minecraft.world.entity.player.Player;
import toughasnails.api.player.ITANPlayer;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.init.ModConfig;

public class ThirstHelperImpl implements ThirstHelper.Impl.IThirstHelper
{
    private IThirst lastThirst;

    @Override
    public IThirst getThirst(Player player)
    {
        IThirst thirst = ((ITANPlayer)player).getThirstData();
        lastThirst = thirst;
        return thirst;
    }

    @Override
    public boolean canDrink(Player player, boolean ignoreThirst)
    {
        IThirst thirst = getThirst(player);
        return (player.getAbilities().invulnerable || ignoreThirst || thirst.isThirsty()) && isThirstEnabled();
    }

    @Override
    public boolean isThirstEnabled()
    {
        return ModConfig.thirst.enableThirst;
    }
}

