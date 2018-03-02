package toughasnails.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.thirst.ThirstHandler;

public class PotionHydration extends TANPotion
{
    public PotionHydration(int id)
    {
        super(true, 0x1B5FD3, 1, 0);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
    	if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;
            ThirstHandler handler = (ThirstHandler)ThirstHelper.getThirstData(player);

            handler.addStats(amplifier + 1, 1.0F);
        }
    }
    
    @Override
    public boolean isReady(int duration, int amplifier)
    {
        return duration >= 1;
    }
    
    @Override
    public boolean isInstant()
    {
        return true;
    }
}
