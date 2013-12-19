package tan.configuration;

import java.io.File;
import java.util.logging.Level;

import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

public class TANConfigurationIDs
{
    public static Configuration config;
    
    public static int canteenID;
    public static int thermometerID;
    public static int freshWaterBottleID;
    
    public static int helmetWoolID;
    public static int chestplateWoolID;
    public static int leggingsWoolID;
    public static int bootsWoolID;

    public static void init(File configFile)
    {
        config = new Configuration(configFile);

        try
        {
            config.load();

            canteenID = config.get("Item IDs", "Canteen ID", 7500).getInt();
            thermometerID = config.get("Item IDs", "Thermometer ID", 7501).getInt();
            freshWaterBottleID = config.get("Item IDs", "Fresh Water Bottle ID", 7502).getInt();
            
            helmetWoolID = config.get("Item IDs", "Wool Helmet ID", 7503).getInt();
            chestplateWoolID = config.get("Item IDs", "Wool Chestplate ID", 7504).getInt();
            leggingsWoolID = config.get("Item IDs", "Wool Leggings ID", 7505).getInt();
            bootsWoolID = config.get("Item IDs", "Wool Boots ID", 7506).getInt();

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

