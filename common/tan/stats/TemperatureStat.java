package tan.stats;

import java.text.DecimalFormat;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import tan.api.TANStat;
import tan.api.temperature.ITemperatureModifier;
import tan.api.temperature.TemperatureRegistry;
import tan.configuration.TANConfigurationTemperature;

public class TemperatureStat extends TANStat
{ 
    @Override
    public void update()
    {    
        if (player.capabilities.isCreativeMode) return;
        
        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);
        
        float originalTemperature = tanData.getFloat(getStatName());
        float temperature = originalTemperature;

        float environmentTemperature = getEnvironmentTemperature(world, x, y, z);
        
        float aimedTemperature = environmentTemperature;
        
        int ratemodifier = 8;
        
        if (player.worldObj.difficultySetting == 0)
        {
        	ratemodifier = 15;
        }
        if (player.worldObj.difficultySetting == 1)
        {
        	ratemodifier = 10;
        }
        if (player.worldObj.difficultySetting == 2)
        {
        	ratemodifier = 8;
        }
        if (player.worldObj.difficultySetting == 3)
        {
        	ratemodifier = 5;
        }
        
        float rate = ((environmentTemperature / 20 / 2) - 0.35F) / ratemodifier /*0.35 "Normal" Temperature (0.7, squashed to fit between 0-1)*/;
        
        rate = (rate < 0 ? -rate : rate);
        
        for (ITemperatureModifier temperatureModifier : TemperatureRegistry.temperatureModifiers)
        {
            aimedTemperature += temperatureModifier.modifyTemperature(world, player);
            rate += temperatureModifier.modifyRate(world, player);
        }
        
        DecimalFormat twoDForm = new DecimalFormat("#.##");   

        try
        {
            aimedTemperature = Float.parseFloat(twoDForm.format(aimedTemperature));
        }
        catch (Exception e)
        {

        }
        
        if (world.rand.nextFloat() <= rate)
        {
            if (temperature > aimedTemperature)
            {
                temperature -= 0.01F;
            }
            else if (temperature < aimedTemperature)
            {
                temperature += 0.01F;
            }
        }
        
        try
        {
            temperature = Float.parseFloat(twoDForm.format(temperature));
        }
        catch (Exception e)
        {

        }
        
        System.out.println("Aimed Temp " + aimedTemperature);
        System.out.println("Rate " + rate); 
        System.out.println("Current Temp " + temperature);
        
        System.out.println("Rate Modifier " + ratemodifier);

        if (temperature != originalTemperature)
        {
            tanData.setFloat(getStatName(), MathHelper.clamp_float(temperature, 27F, 47F));

            updatePlayerData(tanData, player);
        }
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
    
    public static String getTemperatureSymbol()
    {
        String temperatureType = TANConfigurationTemperature.temperatureType;

        if (temperatureType.equals("Farenheit"))
        {
            return "F";
        }
        else if (temperatureType.equals("Kelvin"))
        {
            return "K";
        }
        else
        {
            return "C";
        }
    }
    
    public static int getConvertedDisplayTemperature(int temperature)
    {
        String temperatureType = TANConfigurationTemperature.temperatureType;

        if (temperatureType.equals("Farenheit"))
        {
            return 9 * temperature / 5 + 32; 
        }
        else if (temperatureType.equals("Kelvin"))
        {
            return temperature + 273;
        }
        else
        {
            return temperature;
        }

    }

    @Override
    public void setDefaults()
    {
        setDefaultFloat(getStatName(), 37F);
    }

    @Override
    public String getStatName()
    {
        return "Temp";
    }
}
