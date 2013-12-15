package tan.api.event.temperature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.Event;

public class TemperatureEvent extends Event
{
    public final EntityPlayer player;
    public float temperature;
    
    public TemperatureEvent(EntityPlayer player, float temperature)
    {
        this.player = player;
        this.temperature = temperature;
    }
    
    public static class Rate extends TemperatureEvent
    {
        public float rate;
        
        public Rate(EntityPlayer player, float temperature, float rate)
        {
            super(player, temperature);
            
            this.rate = rate;
        }
    }
}
