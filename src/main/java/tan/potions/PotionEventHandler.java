package tan.potions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import tan.api.utils.TANPlayerStatUtils;
import tan.core.TANPotions;
import tan.stats.TemperatureStat;
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
                ThirstStat thirstStat = TANPlayerStatUtils.getPlayerStat(player, ThirstStat.class);
                
                int amplifier = player.getActivePotionEffect(TANPotions.waterPoisoning).getAmplifier();       
                float exhaustionAmount = 0.095F * (float)(amplifier + 1);     
                
                thirstStat.addExhaustion(exhaustionAmount);
            }
        }
    }
}
