package tan.api.event.thirst;

import net.minecraftforge.event.Event;

public class ThirstEvent extends Event
{
    public int thirstLevel;

    public float thirstHydrationLevel;
    public float thirstExhaustionLevel;

    public final int thirstTimer;
    
    public ThirstEvent(int thirstLevel, float thirstHydrationLevel, float thirstExhaustionLevel, int thirstTimer)
    {
        this.thirstLevel = thirstLevel;
        this.thirstHydrationLevel = thirstHydrationLevel;
        this.thirstExhaustionLevel = thirstExhaustionLevel;
        this.thirstTimer = thirstTimer;
    }
}
