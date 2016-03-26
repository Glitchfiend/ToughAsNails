package toughasnails.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionHyperthermia extends Potion
{
    private static final ResourceLocation POTIONS_LOCATION = new ResourceLocation("toughasnails:textures/potions/TANPotionFX.png");
    
    public PotionHyperthermia(int id)
    {
        super(true, 0xFFA300);
    
        this.setIconIndex(3, 0);
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
        entity.attackEntityFrom(DamageSource.generic, 0.5F);
    }
    
    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int time = 50 >> amplifier;
        return time > 0 ? duration % time == 0 : true;
    }
}
