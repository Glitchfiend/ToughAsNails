package tan.api.temperature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ITemperatureModifier
{
    public float modifyTemperature(World world, EntityPlayer player);
    public float modifyRate(World world, EntityPlayer player);
}
