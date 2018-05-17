package toughasnails.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import toughasnails.api.TANPotions;

public class PotionColdResistance extends TANPotion
{
    public PotionColdResistance(int id)
    {
        super(false, 0x77A9FF, 2, 1);
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
    	entity.removePotionEffect(TANPotions.hypothermia);
    }
    
    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int time = 50 >> amplifier;
        return time > 0 ? duration % time == 0 : true;
    }
}
