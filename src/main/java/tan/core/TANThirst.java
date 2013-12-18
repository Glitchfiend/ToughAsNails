package tan.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import tan.api.thirst.TANDrinkInfo;
import tan.eventhandler.modifiers.ThirstMovementEventHandler;

public class TANThirst
{
    public static void init()
    {
        addDrinkInfo();
        registerThirstModifiers();
    }
    
    private static void addDrinkInfo()
    {
        TANDrinkInfo.addDrinkInfo(FluidRegistry.WATER.getName(), 5, 2F, 0.5F);
    }
    
    private static void registerThirstModifiers()
    {
        MinecraftForge.EVENT_BUS.register(new ThirstMovementEventHandler());
    }
}
