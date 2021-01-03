/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.core.ToughAsNails;

public class ThirstEffect extends Effect
{
    public ThirstEffect(EffectType type, int color)
    {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier)
    {
        if (entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity)entity;
            IThirst thirst = ThirstHelper.getThirst(player);
            thirst.addExhaustion(0.025F * (float)(amplifier + 1));
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier)
    {
        return true;
    }
}
