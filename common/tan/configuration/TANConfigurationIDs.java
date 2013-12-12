package tan.configuration;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

public class TANConfigurationIDs
{
    public static Configuration config;
    
    public static int thermometerID;

    public static void init(File configFile)
    {
        config = new Configuration(configFile);

        try
        {
            config.load();

            thermometerID = config.get("Item IDs", "Thermometer ID", 7500).getInt();

            FMLCommonHandler.instance().getFMLLogger().log(Level.INFO, "[ToughAsNails] Generated ID Config!");
        }
        catch (Exception e)
        {
            FMLLog.log(Level.SEVERE, e, "Tough As Nails has had a problem loading its configuration");
        }
        finally
        {
            if (config.hasChanged()) 
            {
                config.save();
            }
        }
    }
}

