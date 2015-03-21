package toughasnails.init;

import toughasnails.handler.TemperatureOverlayEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ModHandlers
{
    public static void init()
    {
        TemperatureOverlayEventHandler temperatureEventHander = new TemperatureOverlayEventHandler();
        
        FMLCommonHandler.instance().bus().register(temperatureEventHander);
        MinecraftForge.EVENT_BUS.register(temperatureEventHander);
    }
}
