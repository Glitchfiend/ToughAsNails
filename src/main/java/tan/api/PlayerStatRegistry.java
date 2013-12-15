package tan.api;

import java.util.ArrayList;
import java.util.Collection;

public class PlayerStatRegistry
{
    public static ArrayList<TANStat> tanStatList = new ArrayList<TANStat>();
    
    public static void registerStat(TANStat stat)
    {
        tanStatList.add(stat);
    }
}
