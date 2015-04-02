package toughasnails.init;

import toughasnails.api.PlayerStatRegistry;
import toughasnails.temperature.TemperatureStats;
import toughasnails.thirst.ThirstStats;

public class ModStats
{
    public static void init()
    {
        PlayerStatRegistry.addStat("temperature", TemperatureStats.class);
        PlayerStatRegistry.addStat("thirst", ThirstStats.class);
    }
}
