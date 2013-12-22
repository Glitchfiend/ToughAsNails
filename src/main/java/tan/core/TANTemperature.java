package tan.core;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import tan.api.temperature.TemperatureRegistry;
import tan.eventhandler.modifiers.TemperatureArmourEventHandler;
import tan.eventhandler.modifiers.TemperaturePlayerStateEventHandler;
import tan.eventhandler.modifiers.TemperatureSourceEventHandler;
import tan.eventhandler.modifiers.TemperatureTimeEventHandler;

public class TANTemperature
{
    public static void init()
    {
        registerTemperatureModifiers();
        registerBlockTemperatureSources();
        registerArmourTemperatureSources();
    }
    
    private static void registerTemperatureModifiers()
    {
        MinecraftForge.EVENT_BUS.register(new TemperatureSourceEventHandler());
        MinecraftForge.EVENT_BUS.register(new TemperaturePlayerStateEventHandler());
        MinecraftForge.EVENT_BUS.register(new TemperatureArmourEventHandler());
        MinecraftForge.EVENT_BUS.register(new TemperatureTimeEventHandler());
    }
    
    private static void registerBlockTemperatureSources()
    {
        TemperatureRegistry.registerTemperatureSource("B", Block.fire.blockID, -1, 4F, 0.08F);
        TemperatureRegistry.registerTemperatureSource("B", Block.torchWood.blockID, -1, 1F, 0.02F);
        TemperatureRegistry.registerTemperatureSource("B", Block.lavaMoving.blockID, -1, 5F, 0.14F);
        TemperatureRegistry.registerTemperatureSource("B", Block.lavaStill.blockID, -1, 5F, 0.14F);
        TemperatureRegistry.registerTemperatureSource("B", Block.waterMoving.blockID, -1, -0.5F, 0.03F);
        TemperatureRegistry.registerTemperatureSource("B", Block.waterStill.blockID, -1, -0.5F, 0.03F);
        TemperatureRegistry.registerTemperatureSource("B", Block.ice.blockID, 0, -1F, 0.05F);
        TemperatureRegistry.registerTemperatureSource("B", Block.furnaceBurning.blockID, 0, 2.0F, 0.06F);
        TemperatureRegistry.registerTemperatureSource("B", Block.pumpkinLantern.blockID, 0, 0.5F, 0.01F);
        TemperatureRegistry.registerTemperatureSource("B", Block.redstoneLampActive.blockID, 0, 1.5F, 0.04F);
    }
    
    private static void registerArmourTemperatureSources()
    {
        TemperatureRegistry.registerTemperatureSource("A", TANArmour.helmetWool.itemID, 0, 1.75F, 0.05F);
        TemperatureRegistry.registerTemperatureSource("A", TANArmour.chestplateWool.itemID, 0, 1.75F, 0.15F);
        TemperatureRegistry.registerTemperatureSource("A", TANArmour.leggingsWool.itemID, 0, 1.25F, 0.1F);
        TemperatureRegistry.registerTemperatureSource("A", TANArmour.bootsWool.itemID, 0, 0.75F, 0.2F);
        
        TemperatureRegistry.registerTemperatureSource("A", TANArmour.helmetHeat.itemID, 0, -1.75F, 0.05F);
        TemperatureRegistry.registerTemperatureSource("A", TANArmour.chestplateHeat.itemID, 0, -1.75F, 0.15F);
        TemperatureRegistry.registerTemperatureSource("A", TANArmour.leggingsHeat.itemID, 0, -1.25F, 0.1F);
        TemperatureRegistry.registerTemperatureSource("A", TANArmour.bootsHeat.itemID, 0, -0.75F, 0.2F);
    }
}
