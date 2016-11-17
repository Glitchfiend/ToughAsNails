/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import toughasnails.api.season.SeasonHelper;
import toughasnails.block.BlockSeasonSensor;

public class TileEntitySeasonSensor extends TileEntity implements ITickable 
{
    @Override
    public void update()
    {
        if (this.world != null && !this.world.isRemote && SeasonHelper.getSeasonData(this.world).getSeasonCycleTicks() % 20L == 0L)
        {
            ((BlockSeasonSensor)this.getBlockType()).updatePower(this.world, this.pos);
        }
    }
}
