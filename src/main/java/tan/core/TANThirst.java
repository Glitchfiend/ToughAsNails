package tan.core;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import tan.api.thirst.TANDrinkContainer;
import tan.api.thirst.TANDrinkInfo;
import tan.eventhandler.ThirstItemEventHandler;
import tan.eventhandler.modifiers.ThirstMovementEventHandler;

public class TANThirst
{
    public static void init()
    {
        registerDrinks();
        registerThirstModifiers();

        MinecraftForge.EVENT_BUS.register(new ThirstItemEventHandler());
    }
    
    private static void registerDrinks()
    {
        TANDrinkInfo.addDrinkInfo(FluidRegistry.WATER.getName(), 20, 0.8F, 0.5F);

        TANDrinkContainer.addDrinkContainer(Item.potion.itemID);
    }
    
    private static void registerThirstModifiers()
    {
        MinecraftForge.EVENT_BUS.register(new ThirstMovementEventHandler());
    }
}
