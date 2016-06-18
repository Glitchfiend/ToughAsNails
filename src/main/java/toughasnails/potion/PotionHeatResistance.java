package toughasnails.potion;

import net.minecraft.entity.EntityLivingBase;
import toughasnails.api.TANPotions;

public class PotionHeatResistance extends TANPotion
{
    public PotionHeatResistance(int id)
    {
        super(true, 0xE54720, 1, 1);
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
    	entity.removePotionEffect(TANPotions.hyperthermia);
    }
    
    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int time = 50 >> amplifier;
        return time > 0 ? duration % time == 0 : true;
    }
}
