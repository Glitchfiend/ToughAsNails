package tan.eventhandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import tan.api.utils.TANPlayerStatUtils;
import tan.network.PacketTypeHandler;
import tan.network.packet.PacketSendStats;
import tan.stats.TemperatureStat;
import tan.stats.ThirstStat;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class StatUpdateEventHandler
{
    @ForgeSubscribe
    public void onLivingUpdate(LivingUpdateEvent event)
    {
        EntityLivingBase entityLiving = event.entityLiving;
        World world = entityLiving.worldObj;
        
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entityLiving;
            
            if (!world.isRemote)
            {
                TemperatureStat temperatureStat = TANPlayerStatUtils.getPlayerStat(player, TemperatureStat.class);
                ThirstStat thirstStat = TANPlayerStatUtils.getPlayerStat(player, ThirstStat.class);
                
                temperatureStat.update(player);
                thirstStat.update(player);
                
                TANPlayerStatUtils.setPlayerStat(player, temperatureStat);
                TANPlayerStatUtils.setPlayerStat(player, thirstStat);
                
                PacketDispatcher.sendPacketToPlayer(PacketTypeHandler.populatePacket(new PacketSendStats(player)), (Player)player);
            }
        }
    }
}
