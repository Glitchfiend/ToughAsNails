package tan.api.temperature;

import java.util.ArrayList;
import java.util.HashMap;

public class TemperatureRegistry
{
    public static HashMap<String, TemperatureSource> temperatureSources = new HashMap<String, TemperatureSource>();
    
    public static float getTemperatureSourceModifier(int id, int metadata)
    {
        float modifier = 0;
        
        try
        {
            modifier = temperatureSources.get(id + ";" + metadata).temperature;
        }
        catch (Exception e)
        {
            
        }
        return modifier;
    }
    
    public static float getTemperatureSourceRate(int id, int metadata)
    {
        float rate = 0;
        
        try
        {
            rate = temperatureSources.get(id + ";" + metadata).rate;
        }
        catch (Exception e)
        {
            
        }
        return rate;
    }

    public static void registerTemperatureSource(int id, int metadata, float temperatureModifier, float rate)
    { 
        if (metadata == -1)
        {
            for (int i = 0; i < 16; i++)
            {
                temperatureSources.put(id + ";" + i, new TemperatureSource(temperatureModifier, rate));
            }
        }
        else
        {   
            temperatureSources.put(id + ";" + metadata, new TemperatureSource(temperatureModifier, rate));
        }
    }
}
