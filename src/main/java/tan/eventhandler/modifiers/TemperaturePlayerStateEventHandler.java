package tan.eventhandler.modifiers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.ForgeSubscribe;
import tan.api.temperature.TemperatureEvent;

public class TemperaturePlayerStateEventHandler
{
    @ForgeSubscribe
    public void modifyTemperature(TemperatureEvent event)
    {
        EntityPlayer player = event.player;
        
        float modifier = 0F;
        
        if (player.isSprinting()) modifier += 2.25F;
        if (player.isWet()) modifier -= 1.75F;
        if (player.isBurning()) modifier += 4F;
        
        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);
        
        event.temperature += modifier;    
    }
}
