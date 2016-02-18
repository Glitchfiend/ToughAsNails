package toughasnails.init;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import toughasnails.handler.ArrowEventHandler;
import toughasnails.handler.ExtendedStatHandler;
import toughasnails.handler.ModelBakeHandler;
import toughasnails.handler.PacketHandler;
import toughasnails.handler.TemperatureDebugOverlayHandler;
import toughasnails.handler.TemperatureOverlayHandler;
import toughasnails.handler.ThirstOverlayHandler;
import toughasnails.handler.ThirstStatHandler;

public class ModHandlers
{
    public static void init()
    {
        PacketHandler.init();
        
        MinecraftForge.EVENT_BUS.register(new ModelBakeHandler());
        
        ExtendedStatHandler extendedStatHandler = new ExtendedStatHandler();
        
        FMLCommonHandler.instance().bus().register(extendedStatHandler);
        MinecraftForge.EVENT_BUS.register(new ExtendedStatHandler());
        
        TemperatureOverlayHandler temperatureOverlayHandler = new TemperatureOverlayHandler();

        FMLCommonHandler.instance().bus().register(temperatureOverlayHandler);
        MinecraftForge.EVENT_BUS.register(temperatureOverlayHandler);
        MinecraftForge.EVENT_BUS.register(new TemperatureDebugOverlayHandler());
        
        ThirstOverlayHandler thirstOverlayHandler = new ThirstOverlayHandler();
        ThirstStatHandler thirstStatHandler = new ThirstStatHandler();
        
        FMLCommonHandler.instance().bus().register(thirstOverlayHandler);
        MinecraftForge.EVENT_BUS.register(thirstOverlayHandler);
        FMLCommonHandler.instance().bus().register(thirstStatHandler);
        MinecraftForge.EVENT_BUS.register(thirstStatHandler);
        
        ArrowEventHandler arrowEventHandler = new ArrowEventHandler();
        
        FMLCommonHandler.instance().bus().register(arrowEventHandler);
        MinecraftForge.EVENT_BUS.register(arrowEventHandler);
    }
}
