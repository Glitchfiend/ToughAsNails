package tan.api.utils;

import java.text.DecimalFormat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import tan.api.event.temperature.TemperatureEvent;

public class TemperatureUtils
{
    public static float getAimedTemperature(float environmentTemperature, World world, EntityPlayer player)
    {
        float aimedTemperature = environmentTemperature;
        
        TemperatureEvent temperatureEvent = new TemperatureEvent(player, aimedTemperature);
        
        MinecraftForge.EVENT_BUS.post(temperatureEvent);
        aimedTemperature = temperatureEvent.temperature;
        
        DecimalFormat twoDForm = new DecimalFormat("#.##");   

        try
        {
            aimedTemperature = Float.parseFloat(twoDForm.format(aimedTemperature));
        }
        catch (Exception e)
        {

        }

        return aimedTemperature;
    }
    
    public static float getEnvironmentTemperature(World world, int x, int y, int z)
    {
        float averageAimedEnvironmentTemperature = 0F;

        int environmentDivider = 0;

        for (int ix = -2; ix <= 2; ix++)
        {
            for (int iy = -1; iy <= 1; iy++)
            {
                for (int iz = -2; iz <= 2; iz++)
                {
                    int blockID = world.getBlockId(x + ix, y + iy, z + iz);
                    int metadata = world.getBlockMetadata(x + ix, y + iy, z + iz);
                    
                    BiomeGenBase biome = world.getBiomeGenForCoords(x + ix, z + iz);

                    averageAimedEnvironmentTemperature += ((biome.temperature / 2) * 20) + 27;

                    environmentDivider++;
                }
            }
        }
        
        return averageAimedEnvironmentTemperature / environmentDivider;
    }
    
}
