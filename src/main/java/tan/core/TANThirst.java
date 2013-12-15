package tan.core;

import net.minecraftforge.common.MinecraftForge;
import tan.eventhandler.thirstmodifier.ThirstMovementEventHandler;

public class TANThirst
{
    public static void init()
    {
        registerThirstModifiers();
    }
    
    private static void registerThirstModifiers()
    {
        MinecraftForge.EVENT_BUS.register(new ThirstMovementEventHandler());
    }
}
