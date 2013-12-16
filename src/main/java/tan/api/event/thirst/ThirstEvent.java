package tan.api.event.thirst;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.Event;
import tan.stats.ThirstStat;

public class ThirstEvent extends Event
{
    public final EntityPlayer player;
    
    public ThirstStat thirstStat;
    
    public ThirstEvent(EntityPlayer player, ThirstStat thirstStat)
    {
        this.player = player;
        
        this.thirstStat = thirstStat;
    }
}
