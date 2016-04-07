package toughasnails.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.thirst.ThirstHandler;

public class PotionThirst extends TANPotion
{
    public PotionThirst(int id)
    {
        super(true, 0x61D51A, 0, 0);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
    	if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;
            ThirstHandler handler = (ThirstHandler)ThirstHelper.getThirstData(player);

            handler.addExhaustion(0.025F * (float)(amplifier + 1));
        }
    }
    
    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int time = 50 >> amplifier;
        return time > 0 ? duration % time == 0 : true;
    }
}
