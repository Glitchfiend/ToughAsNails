package toughasnails.init;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import toughasnails.handler.PacketHandler;
import toughasnails.handler.TemperatureDebugOverlayHandler;
import toughasnails.handler.TemperatureOverlayEventHandler;
import toughasnails.handler.TemperatureStatHandler;
import toughasnails.handler.ThirstOverlayHandler;

public class ModHandlers
{
    public static void init()
    {
        PacketHandler.init();
        
        TemperatureOverlayEventHandler temperatureEventHander = new TemperatureOverlayEventHandler();
        TemperatureStatHandler temperatureStatHandler = new TemperatureStatHandler();
        
        FMLCommonHandler.instance().bus().register(temperatureEventHander);
        FMLCommonHandler.instance().bus().register(temperatureStatHandler);
        MinecraftForge.EVENT_BUS.register(temperatureEventHander);
        MinecraftForge.EVENT_BUS.register(new TemperatureStatHandler());
        MinecraftForge.EVENT_BUS.register(new TemperatureDebugOverlayHandler());
        
        MinecraftForge.EVENT_BUS.register(new ThirstOverlayHandler());
    }
}
