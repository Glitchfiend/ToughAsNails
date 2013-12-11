package tan.configuration;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

public class TANConfigurationTemperature
{
    public static Configuration config;
    
    public static String temperatureType;

    public static void init(File configFile)
    {
        config = new Configuration(configFile);

        try
        {
            config.load();

            temperatureType = config.get("Temperature", "Temperature Type", "Celsius", "The type of temperature to use, supports Celsius, Farenheit & Kelvin (Metrics/Celsius is better - Adubbz :P)").getString();

            FMLCommonHandler.instance().getFMLLogger().log(Level.INFO, "[ToughAsNails] Generated Temperature Config!");
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
