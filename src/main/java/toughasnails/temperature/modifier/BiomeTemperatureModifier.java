package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import toughasnails.temperature.TemperatureInfo;
import toughasnails.temperature.TemperatureScale;

public class BiomeTemperatureModifier implements ITemperatureModifier
{
    public static final int TEMPERATURE_SCALE_MIDPOINT = TemperatureScale.getScaleTotal() / 2;
    
    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        return changeRate;
    }

    @Override
    public TemperatureInfo modifyTarget(World world, EntityPlayer player, TemperatureInfo temperature)
    {
        BiomeGenBase biome = world.getBiomeGenForCoords(player.getPosition());
        float biomeTemperature = biome.temperature;
        int newTemperatureLevel = TEMPERATURE_SCALE_MIDPOINT + (int)((biomeTemperature - 0.5F) * 5.0F);
        
        return new TemperatureInfo(MathHelper.clamp_int(newTemperatureLevel, 0, TemperatureScale.getScaleTotal()));
    }
}
