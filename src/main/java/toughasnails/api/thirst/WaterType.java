/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.thirst;

public enum WaterType
{
    NORMAL(0.5F), DIRTY(1.0F), PURIFIED(0.0F);

    private float poisonChance;

    WaterType(float poisonChance)
    {
        this.poisonChance = poisonChance;
    }

    public float getPoisonChance()
    {
        return this.poisonChance;
    }
}
