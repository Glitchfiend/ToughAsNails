/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.config.json;

public class DrinkData
{
    private ItemPredicate item;
    private int thirst;
    private float hydration;
    private float poisonChance;

    public DrinkData(ItemPredicate item, int thirst, float hydration, float poisonChance)
    {
        this.item = item;
        this.thirst = thirst;
        this.hydration = hydration;
        this.poisonChance = poisonChance;
    }

    public ItemPredicate getPredicate()
    {
        return item;
    }

    public int getThirstRestored()
    {
        return this.thirst;
    }

    public float getHydrationRestored()
    {
        return this.hydration;
    }

    public float getPoisonChance()
    {
        return this.poisonChance;
    }
}
