/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

import toughasnails.api.item.ItemDrink;
import toughasnails.api.thirst.WaterType;

public class ItemPurifiedWaterBottle extends ItemDrink<WaterType>
{
    @Override
    public WaterType getTypeFromMeta(int meta)
    {
    	return WaterType.PURIFIED;
    }
}
