package tan.temperaturemodifiers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tan.api.temperature.ITemperatureModifier;

public class TemperaturePlayerStateModifier implements ITemperatureModifier
{
    @Override
    public float modifyTemperature(World world, EntityPlayer player)
    {
        float modifier = 0F;
        
        if (player.isSprinting()) modifier += 2.25F;
        if (player.isWet()) modifier -= 2.5F;
        if (player.isBurning()) modifier += 4F;
        
        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);
        
        return modifier;
    }

    @Override
    public float modifyRate(World world, EntityPlayer player)
    {
        return 0F;
    }
}
