package tan.api.event.thirst;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.Event;

public class ThirstEvent extends Event
{
    public final EntityPlayer player;
    
    public int thirstLevel;

    public float thirstHydrationLevel;
    public float thirstExhaustionLevel;

    public final int thirstTimer;
    
    public ThirstEvent(EntityPlayer player, int thirstLevel, float thirstHydrationLevel, float thirstExhaustionLevel, int thirstTimer)
    {
        this.player = player;
        
        this.thirstLevel = thirstLevel;
        this.thirstHydrationLevel = thirstHydrationLevel;
        this.thirstExhaustionLevel = thirstExhaustionLevel;
        this.thirstTimer = thirstTimer;
    }
}
