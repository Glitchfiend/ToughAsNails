/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

public class PurifiedWaterBottleItem extends DrinkItem
{
    public PurifiedWaterBottleItem(Properties properties)
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
        return 6;
    }

    @Override
    public float getReplenishedHydration()
    {
        return 0.5F;
    }
}
