package toughasnails.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;

public class PotionHypothermia extends TANPotion
{
    public PotionHypothermia(int id)
    {
        super(true, 0xA8CFFF, 2, 0);
    
        this.setIconIndex(2, 0);
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
    	EnumDifficulty enumdifficulty = entity.getEntityWorld().getDifficulty();
    	if (!(entity instanceof EntityPlayer) || (enumdifficulty == EnumDifficulty.EASY && entity.getHealth() > 10.0F) || (enumdifficulty == EnumDifficulty.NORMAL && entity.getHealth() > 1.0F) || enumdifficulty == EnumDifficulty.HARD)
    	{
    		entity.attackEntityFrom(DamageSource.GENERIC, 0.5F);
    	}
    }
    
    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int time = 50 >> amplifier;
        return time > 0 ? duration % time == 0 : true;
    }
}
