package tan.api.temperature;

import java.util.ArrayList;
import java.util.HashMap;

public class TemperatureRegistry
{
    public static HashMap<String, TemperatureSource> temperatureSources = new HashMap<String, TemperatureSource>();
    
    public static float getTemperatureSourceModifier(String type, int id, int metadata)
    {
        float modifier = 0;
        
        try
        {
            modifier = temperatureSources.get(type + ";" + id + ";" + metadata).temperature;
        }
        catch (Exception e)
        {
            
        }
        return modifier;
    }
    
    public static float getTemperatureSourceRate(String type, int id, int metadata)
    {
        float rate = 0;
        
        try
        {
            rate = temperatureSources.get(type + ";" + id + ";" + metadata).rate;
        }
        catch (Exception e)
        {
            
        }
        return rate;
    }

    public static void registerTemperatureSource(String type, int id, int metadata, float temperatureModifier, float rate)
    { 
        if (metadata == -1)
        {
            for (int i = 0; i < 16; i++)
            {
                temperatureSources.put(type + ";" + id + ";" + i, new TemperatureSource(temperatureModifier, rate));
            }
        }
        else
        {   
            temperatureSources.put(type + ";" + id + ";" + metadata, new TemperatureSource(temperatureModifier, rate));
        }
    }
}
