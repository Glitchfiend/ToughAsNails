/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.thirst;

public enum WaterType 
{
    DIRTY(4, 0.4F, 0.5F), 
    FILTERED(6, 0.6F, 0.1F), 
    CLEAN(8, 0.8F, 0.0F);
    
    private int thirst;
    private float hydration;
    private float poisonChance;
    
    private WaterType(int thirst, float hydration, float poisonChance)
    {
        this.thirst = thirst;
        this.hydration = hydration;
        this.poisonChance = poisonChance;
    }
}
