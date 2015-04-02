package toughasnails.init;

import toughasnails.api.PlayerStatRegistry;
import toughasnails.temperature.TemperatureStats;

public class ModStats
{
    public static void init()
    {
        PlayerStatRegistry.addStat("temperature", TemperatureStats.class);
    }
}
