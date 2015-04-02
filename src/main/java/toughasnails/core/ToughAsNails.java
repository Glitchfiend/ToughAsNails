package toughasnails.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import toughasnails.command.TANCommand;
import toughasnails.init.ModHandlers;
import toughasnails.init.ModPotions;
import toughasnails.init.ModStats;

@Mod(modid = ToughAsNails.MOD_ID, name = ToughAsNails.MOD_NAME)
public class ToughAsNails
{
    public static final String MOD_NAME = "Tough As Nails";
    public static final String MOD_ID = "ToughAsNails";
    
    @Instance(MOD_ID)
    public static ToughAsNails instance;
    
    public static Logger logger = LogManager.getLogger(MOD_ID);
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModStats.init();
        ModPotions.init();
        ModHandlers.init();
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new TANCommand());
    }
}
