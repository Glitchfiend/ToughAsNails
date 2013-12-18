package tan.eventhandler.modifiers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import tan.api.temperature.TemperatureEvent;

public class TemperatureTimeEventHandler
{
    @ForgeSubscribe
    public void modifyTemperature(TemperatureEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.worldObj;
        
        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);
        
        if (isNight(world)) 
        {
            if (world.getChunkFromChunkCoords(x >> 4, z >> 4).canBlockSeeTheSky(x & 15, y + 1, z & 15))
            {
                if (world.isRaining())
                {
                    event.temperature += -3.5F;
                }
                else
                {
                    event.temperature += -2F;
                }
            }
        }
        else
        {
            if (world.getChunkFromChunkCoords(x >> 4, z >> 4).canBlockSeeTheSky(x & 15, y + 1, z & 15))
            {
                if (world.isRaining())
                {
                    event.temperature += -1.75F;
                }
            }
        }
    }
    
    public boolean isDay(World world)
    {
        float celestialAngle = world.getCelestialAngle(0.0F);
        
        if (celestialAngle >= 0.75F && celestialAngle <= 1.0F || celestialAngle >= 0.0F && celestialAngle <= 0.25F) return true;
        
        return false;
    }
    
    public boolean isNight(World world)
    {
        float celestialAngle = world.getCelestialAngle(0.0F);

        if (celestialAngle >= 0.25F && celestialAngle <= 0.75F) return true;
        
        return false;
    }
}
