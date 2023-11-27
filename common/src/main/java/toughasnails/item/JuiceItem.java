/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.item;

public class JuiceItem extends DrinkItem
{
    public JuiceItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean canAlwaysDrink()
    {
        return false;
    }
}
