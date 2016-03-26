package toughasnails.init;

import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.PlayerStatRegistry;
import toughasnails.api.stat.capability.ITemperature;
import toughasnails.api.stat.capability.IThirst;
import toughasnails.temperature.TemperatureHandler;
import toughasnails.temperature.TemperatureStorage;
import toughasnails.thirst.ThirstHandler;
import toughasnails.thirst.ThirstStorage;

public class ModStats
{
    public static void init()
    {
        PlayerStatRegistry.addStat(ITemperature.class, new TemperatureStorage(), TemperatureHandler.class);
        PlayerStatRegistry.addStat(IThirst.class, new ThirstStorage(), ThirstHandler.class);
        
        //These MUST be registered after stats are added, as only then will ours capabilities be non-null
        PlayerStatRegistry.registerCapability(TANCapabilities.TEMPERATURE);
        PlayerStatRegistry.registerCapability(TANCapabilities.THIRST);
    }
}
