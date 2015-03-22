package toughasnails.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.network.message.MessageUpdateTemperature;
import toughasnails.temperature.TemperatureStats;

public class TemperatureStatHandler
{
    @SubscribeEvent
    public void onPlayerConstructing(EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.entity;
            
            player.registerExtendedProperties("temperature", new TemperatureStats());
        }
    }
    
    @SubscribeEvent
    public void onPlayerUpdate(LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.entityLiving;
        World world = entity.worldObj;
        
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;
            TemperatureStats temperatureStats = (TemperatureStats)player.getExtendedProperties("temperature");
            int temperatureLevel = temperatureStats.getTemperature().getScalePos();
            
            if (temperatureStats.getPrevTemperature().getScalePos() != temperatureLevel)
            {
                temperatureStats.setPrevTemperature(temperatureLevel);
                
                if (!world.isRemote)
                {
                    PacketHandler.instance.sendTo(new MessageUpdateTemperature(temperatureLevel), (EntityPlayerMP)player);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        World world = event.world;
        IBlockState blockState = world.getBlockState(event.pos);
        EntityPlayer player = event.entityPlayer;
        TemperatureStats temperatureStats = (TemperatureStats)player.getExtendedProperties("temperature");
        
        int temperatureLevel = temperatureStats.getTemperature().getScalePos();
        
        if (event.action == Action.RIGHT_CLICK_BLOCK)
        {
            temperatureStats.addTemperature(1);
        }
        else if (event.action == Action.LEFT_CLICK_BLOCK)
        {
            temperatureStats.addTemperature(-1);
        }
        
        System.out.println(temperatureStats.getTemperature().getScalePos());
    }
}
