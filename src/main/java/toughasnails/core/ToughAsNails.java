package toughasnails.core;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import toughasnails.init.ModHandlers;

@Mod(modid = ToughAsNails.MOD_ID, name = ToughAsNails.MOD_NAME)
public class ToughAsNails
{
    public static final String MOD_NAME = "Tough As Nails";
    public static final String MOD_ID = "ToughAsNails";
    
    @Instance(MOD_ID)
    public static ToughAsNails instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModHandlers.init();
    }
}
