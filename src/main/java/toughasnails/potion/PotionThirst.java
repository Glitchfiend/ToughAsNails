package toughasnails.potion;

import toughasnails.api.TANPotions;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.thirst.ThirstHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionThirst extends Potion
{
    private static final ResourceLocation POTIONS_LOCATION = new ResourceLocation("toughasnails:textures/potions/TANPotionFX.png");
    
    public PotionThirst(int id)
    {
        super(true, 0x61D51A);
    
        this.setIconIndex(0, 0);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex()
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(POTIONS_LOCATION);
        
        return super.getStatusIconIndex();
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
