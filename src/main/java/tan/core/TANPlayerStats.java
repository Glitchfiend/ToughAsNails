package tan.core;

import tan.api.PlayerStatRegistry;
import tan.stats.TemperatureStat;
import tan.stats.ThirstStat;

public class TANPlayerStats
{
    public static void init()
    {
        registerStats();
    }
    
    private static void registerStats()
    {
        PlayerStatRegistry.registerStat(new TemperatureStat());
        PlayerStatRegistry.registerStat(new ThirstStat());
    }
}
