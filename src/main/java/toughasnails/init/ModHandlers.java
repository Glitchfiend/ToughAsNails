package toughasnails.init;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import toughasnails.handler.PacketHandler;
import toughasnails.handler.TemperatureDebugOverlayHandler;
import toughasnails.handler.TemperatureOverlayHandler;
import toughasnails.handler.ExtendedStatHandler;
import toughasnails.handler.ThirstOverlayHandler;
import toughasnails.handler.ThirstStatHandler;

public class ModHandlers
{
    public static void init()
    {
        PacketHandler.init();
        
        ExtendedStatHandler extendedStatHandler = new ExtendedStatHandler();
        
        FMLCommonHandler.instance().bus().register(extendedStatHandler);
        MinecraftForge.EVENT_BUS.register(new ExtendedStatHandler());
        
        TemperatureOverlayHandler temperatureOverlayHandler = new TemperatureOverlayHandler();

        FMLCommonHandler.instance().bus().register(temperatureOverlayHandler);
        MinecraftForge.EVENT_BUS.register(temperatureOverlayHandler);
        MinecraftForge.EVENT_BUS.register(new TemperatureDebugOverlayHandler());
        
        MinecraftForge.EVENT_BUS.register(new ThirstOverlayHandler());
        
        ThirstStatHandler thirstStatHandler = new ThirstStatHandler();
        
        FMLCommonHandler.instance().bus().register(thirstStatHandler);
        MinecraftForge.EVENT_BUS.register(thirstStatHandler);
    }
}
