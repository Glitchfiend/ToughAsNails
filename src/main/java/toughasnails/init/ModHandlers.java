package toughasnails.init;

import toughasnails.handler.PacketHandler;
import toughasnails.handler.TemperatureOverlayEventHandler;
import toughasnails.handler.TemperatureStatHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ModHandlers
{
    public static void init()
    {
        PacketHandler.init();
        
        TemperatureOverlayEventHandler temperatureEventHander = new TemperatureOverlayEventHandler();
        
        FMLCommonHandler.instance().bus().register(temperatureEventHander);
        MinecraftForge.EVENT_BUS.register(temperatureEventHander);
        MinecraftForge.EVENT_BUS.register(new TemperatureStatHandler());
    }
}
