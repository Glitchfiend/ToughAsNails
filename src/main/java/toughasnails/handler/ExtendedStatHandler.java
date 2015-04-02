package toughasnails.handler;

import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import toughasnails.api.PlayerStat;
import toughasnails.api.PlayerStatRegistry;

public class ExtendedStatHandler
{
    @SubscribeEvent
    public void onPlayerConstructing(EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.entity;
            
            for (String identifier : PlayerStatRegistry.getStatMap().keySet())
            {
                player.registerExtendedProperties(identifier, PlayerStatRegistry.createStat(identifier));
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.worldObj;
        
        if (!world.isRemote)
        {
            for (String identifier : PlayerStatRegistry.getStatMap().keySet())
            {
                PlayerStat stat = (PlayerStat)player.getExtendedProperties(identifier);
                
                stat.onSendClientUpdate();
                PacketHandler.instance.sendTo(stat.createUpdateMessage(), (EntityPlayerMP)player);
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.worldObj;

        if (!world.isRemote)
        {
            if (event.phase == Phase.END)
            {
                for (String identifier : PlayerStatRegistry.getStatMap().keySet())
                {
                    PlayerStat stat = (PlayerStat)player.getExtendedProperties(identifier);

                    stat.update(player, world);

                    if (stat.shouldUpdateClient())
                    {
                        stat.onSendClientUpdate();
                        PacketHandler.instance.sendTo(stat.createUpdateMessage(), (EntityPlayerMP)player);
                    }
                }
            }
        }
    }
}
