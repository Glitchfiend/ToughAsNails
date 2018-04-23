package toughasnails.init;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import toughasnails.config.SyncedConfigHandler;
import toughasnails.handler.BucketEventHandler;
import toughasnails.handler.ExtendedStatHandler;
import toughasnails.handler.PacketHandler;
import toughasnails.handler.temperature.TemperatureOverlayHandler;
import toughasnails.handler.temperature.TemperatureStatTableHandler;
import toughasnails.handler.thirst.DrinkHandler;
import toughasnails.handler.thirst.ThirstOverlayHandler;
import toughasnails.handler.thirst.ThirstStatHandler;

public class ModHandlers
{
    public static void init()
    {
        PacketHandler.init();

        MinecraftForge.EVENT_BUS.register(new ExtendedStatHandler());
        MinecraftForge.EVENT_BUS.register(new SyncedConfigHandler());
        MinecraftForge.EVENT_BUS.register(new BucketEventHandler());
        MinecraftForge.EVENT_BUS.register(new ThirstStatHandler());
        MinecraftForge.EVENT_BUS.register(new DrinkHandler());
        
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            MinecraftForge.EVENT_BUS.register(new TemperatureOverlayHandler());
            MinecraftForge.EVENT_BUS.register(new TemperatureStatTableHandler());
            MinecraftForge.EVENT_BUS.register(new ThirstOverlayHandler());
        }
    }
}
