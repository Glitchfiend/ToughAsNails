package tan.potions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import tan.core.TANPotions;
import tan.stats.ThirstStat;

public class PotionEventHandler
{
    @ForgeSubscribe
    public void onEntityLivingUpdate(LivingUpdateEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            
            if (event.entityLiving.isPotionActive(TANPotions.waterPoisoning))
            {
                int amplifier = player.getActivePotionEffect(TANPotions.waterPoisoning).getAmplifier();       
                float exhaustionAmount = 0.095F * (float)(amplifier + 1);             
                float thirstExhaustionLevel = ThirstStat.addExhaustion(player.getEntityData().getCompoundTag("ToughAsNails").getCompoundTag("thirst").getFloat("thirstExhaustionLevel"), exhaustionAmount);
                
                player.getEntityData().getCompoundTag("ToughAsNails").getCompoundTag("thirst").setFloat("thirstExhaustionLevel", thirstExhaustionLevel);
            }
        }
    }
}
