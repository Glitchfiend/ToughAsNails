package tan.core;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import tan.api.temperature.TemperatureRegistry;
import tan.eventhandler.temperaturemodifier.TemperaturePlayerStateEventHandler;
import tan.eventhandler.temperaturemodifier.TemperatureSourceEventHandler;
import tan.eventhandler.temperaturemodifier.TemperatureTimeEventHandler;

public class TANTemperature
{
    public static void init()
    {
        registerTemperatureModifiers();
        registerTemperatureSources();
    }
    
    private static void registerTemperatureModifiers()
    {
        MinecraftForge.EVENT_BUS.register(new TemperatureSourceEventHandler());
        MinecraftForge.EVENT_BUS.register(new TemperaturePlayerStateEventHandler());
        MinecraftForge.EVENT_BUS.register(new TemperatureTimeEventHandler());
    }
    
    private static void registerTemperatureSources()
    {
        TemperatureRegistry.registerTemperatureSource(Block.fire.blockID, -1, 4F, 0.08F);
        TemperatureRegistry.registerTemperatureSource(Block.torchWood.blockID, -1, 1F, 0.02F);
        TemperatureRegistry.registerTemperatureSource(Block.lavaMoving.blockID, -1, 5F, 0.14F);
        TemperatureRegistry.registerTemperatureSource(Block.lavaStill.blockID, -1, 5F, 0.14F);
        TemperatureRegistry.registerTemperatureSource(Block.waterMoving.blockID, -1, -0.5F, 0.03F);
        TemperatureRegistry.registerTemperatureSource(Block.waterStill.blockID, -1, -0.5F, 0.03F);
        TemperatureRegistry.registerTemperatureSource(Block.ice.blockID, 0, -1F, 0.05F);
    }
}
