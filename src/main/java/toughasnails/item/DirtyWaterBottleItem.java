/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

public class DirtyWaterBottleItem extends DrinkItem
{
    public DirtyWaterBottleItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean canAlwaysDrink()
    {
        return false;
    }

    @Override
    public int getReplenishedThirst()
    {
        return 3;
    }

    @Override
    public float getReplenishedHydration()
    {
        return 0.25F;
    }
}
