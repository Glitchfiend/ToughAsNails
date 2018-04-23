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
    NORMAL("Water", 3, 0.1F, 0.75F), 
    PURIFIED("Purified Water", 6, 0.5F, 0.0F);
    
    private String description;
    private int thirst;
    private float hydration;
    private float poisonChance;
    
    private WaterType(String description, int thirst, float hydration, float poisonChance)
    {
        this.description = description;
        this.thirst = thirst;
        this.hydration = hydration;
        this.poisonChance = poisonChance;
    }
    
    public String getDescription()
    {
        return this.description;
    }
    
    public int getThirst()
    {
        return this.thirst;
    }
    
    public float getHydration()
    {
        return this.hydration;
    }
    
    public float getPoisonChance()
    {
        return this.poisonChance;
    }
}
