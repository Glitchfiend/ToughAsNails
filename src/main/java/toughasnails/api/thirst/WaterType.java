package toughasnails.api.thirst;

/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
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
