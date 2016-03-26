package toughasnails.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import toughasnails.api.stat.IPlayerStat;
import toughasnails.api.stat.PlayerStatRegistry;
import toughasnails.api.stat.StatHandlerBase;
import toughasnails.api.stat.capability.CapabilityProvider;
import toughasnails.core.ToughAsNails;
import toughasnails.temperature.TemperatureHandler;

public class ExtendedStatHandler
{
    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent.Entity event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.getEntity();
            
            for (String identifier : PlayerStatRegistry.getCapabilityMap().keySet())
            {
                ResourceLocation loc = new ResourceLocation(ToughAsNails.MOD_ID, identifier);
                
                //Each player should have their own instance for each stat, as associated values may vary
                if (!event.getCapabilities().containsKey(loc))
                    event.addCapability(loc, PlayerStatRegistry.createCapabilityProvider(identifier));
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
            for (Capability capability : PlayerStatRegistry.getCapabilityMap().values())
            {
                StatHandlerBase stat = (StatHandlerBase)player.getCapability(capability, null);
                
                capability.getStorage().readNBT(capability, stat, null, player.getEntityData().getCompoundTag(capability.getName()));
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
            for (Capability capability : PlayerStatRegistry.getCapabilityMap().values())
            {
                IPlayerStat stat = (IPlayerStat)player.getCapability(capability, null);

                stat.update(player, world, event.phase);

                if (event.phase == Phase.END)
                {
                    if (stat.hasChanged())
                    {
                        player.getEntityData().setTag(capability.getName(), capability.getStorage().writeNBT(capability, stat, null));
                        stat.onSendClientUpdate();
                        PacketHandler.instance.sendTo(stat.createUpdateMessage(), (EntityPlayerMP)player);
                    }
                }
            }
        }
    }
}
