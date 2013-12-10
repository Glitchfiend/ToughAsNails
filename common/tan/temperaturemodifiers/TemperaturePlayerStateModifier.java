package tan.temperaturemodifiers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tan.api.temperature.ITemperatureModifier;

public class TemperaturePlayerStateModifier implements ITemperatureModifier
{
    @Override
    public float modifyTemperature(World world, EntityPlayerMP player)
    {
        float modifier = 0F;
        
        if (player.isSprinting()) modifier += 2.25F;
        if (player.isWet()) modifier -= 2.5F;
        if (player.isBurning()) modifier += 4F;
        
        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);
        
        if (world.getChunkFromChunkCoords(x >> 4, z >> 4).canBlockSeeTheSky(x & 15, y, z & 15))
        {
        	modifier -= 2F;
        }
        else
        {
        	modifier += 2F;
        }
        
        return modifier;
    }

    @Override
    public float modifyRate(World world, EntityPlayerMP player)
    {
        return 0F;
    }
}
