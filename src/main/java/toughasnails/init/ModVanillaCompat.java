package toughasnails.init;

import net.minecraft.block.BlockDispenser;
import toughasnails.api.TANItems;
import toughasnails.entities.projectile.DispenserBehaviorTANArrow;

public class ModVanillaCompat
{
    public static void init()
    {
    	registerDispenserBehaviors();
    }
    
    private static void registerDispenserBehaviors()
    {
    	BlockDispenser.dispenseBehaviorRegistry.putObject(TANItems.arrow, new DispenserBehaviorTANArrow());
    }
}
