package tan.temperaturemodifiers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tan.api.temperature.ITemperatureModifier;

public class TemperatureTimeModifier implements ITemperatureModifier
{
    @Override
    public float modifyTemperature(World world, EntityPlayerMP player)
    {
    	int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);
    	
        if (isNight(world)) 
    	{
        	if (world.getChunkFromChunkCoords(x >> 4, z >> 4).canBlockSeeTheSky(x & 15, y + 1, z & 15))
        	{
        		if (world.isRaining())
        		{
        			return -3.5F;
        		}
        		else
        		{
        			return -2F;
        		}
        	}
    	}
        else
        {
        	if (world.getChunkFromChunkCoords(x >> 4, z >> 4).canBlockSeeTheSky(x & 15, y + 1, z & 15))
        	{
        		if (world.isRaining())
        		{
        			return -1.75F;
        		}
        	}
        }
        
        return 0F;
    }
    
    @Override
    public float modifyRate(World world, EntityPlayerMP player)
    {
        return 0F;
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
