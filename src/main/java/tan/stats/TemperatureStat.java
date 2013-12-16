package tan.stats;

import java.text.DecimalFormat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import tan.api.TANStat;
import tan.api.event.temperature.TemperatureEvent;
import tan.api.utils.TemperatureUtils;
import tan.configuration.TANConfigurationTemperature;
import tan.core.TANDamageSource;

public class TemperatureStat extends TANStat
{ 
    public float temperatureLevel = 37F;
    
    public int temperatureTimer = 0;
    
    @Override
    public void update(EntityPlayer player)
    {    
        World world = player.worldObj;
        
        if (player.capabilities.isCreativeMode) return;
        
        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);

        float environmentTemperature = TemperatureUtils.getEnvironmentTemperature(world, x, y, z);
        
        float aimedTemperature = TemperatureUtils.getAimedTemperature(environmentTemperature, world, player);
        
        int difficultySetting = player.worldObj.difficultySetting;
        
        int ratemodifier;
        
        switch (difficultySetting)
        {
            case 0:
                ratemodifier = 15;
                break;
                
            case 1:
                ratemodifier = 10;
                break;
                
            case 2:
                ratemodifier = 8;
                break;
                
            case 3:
                ratemodifier = 5;
                break;
                
            default:
                ratemodifier = 8;
                break;
        }
        
        float rate = ((environmentTemperature / 20 / 2) - 0.35F) / ratemodifier /*0.35 "Normal" Temperature (0.7, squashed to fit between 0-1)*/;
        
        rate = (rate < 0 ? -rate : rate);
        
        TemperatureEvent.Rate rateEvent = new TemperatureEvent.Rate(player, aimedTemperature, rate);
        
        MinecraftForge.EVENT_BUS.post(rateEvent);
        rate = rateEvent.rate;
        
        DecimalFormat twoDForm = new DecimalFormat("#.##");  
        
        if (world.rand.nextFloat() <= rate)
        {
            if (temperatureLevel > aimedTemperature)
            {
                temperatureLevel -= 0.01F;
            }
            else if (temperatureLevel < aimedTemperature)
            {
                temperatureLevel += 0.01F;
            }
        }
        
        try
        {
            temperatureLevel = Float.parseFloat(twoDForm.format(temperatureLevel));
        }
        catch (Exception e)
        {

        }
        
        DamageSource damageSource = null;
        
        if (temperatureLevel > 44F)
        {
            temperatureTimer++;
            
            if (temperatureLevel < 46F && this.temperatureTimer >= 320F || temperatureLevel >= 46F && temperatureLevel < 47F && temperatureTimer >= 120F || temperatureLevel >= 47F && temperatureLevel < 48F && temperatureTimer >= 40F)
            {
                damageSource = TANDamageSource.hyperthermia;
            }
        }
        else if (temperatureLevel < 30F)
        {
            temperatureTimer++;
            
            if (temperatureLevel > 28F && this.temperatureTimer >= 320F || temperatureLevel <= 28F && temperatureLevel > 27 && temperatureTimer >= 120F || temperatureLevel <= 27F && temperatureLevel > 26F && temperatureTimer >= 40F)
            {
                damageSource = TANDamageSource.hypothermia;
            }
        }
        else
        {
            temperatureTimer = 0;
        }
        
        if (damageSource != null)
        {
            if (player.getHealth() > 10.0F || difficultySetting >= 3 || player.getHealth() > 1.0F && difficultySetting >= 2)
            {
                player.attackEntityFrom(damageSource, 1.0F);
            }

            this.temperatureTimer = 0;
        }
        
        //System.out.println("Aimed Temp " + aimedTemperature);
        //System.out.println("Rate " + rate); 
        //System.out.println("Current Temp " + temperatureLevel);
        //System.out.println("Rate Modifier " + ratemodifier);
    }
    
    @Override
    public void readNBT(NBTTagCompound tanData)
    {
        if (tanData.hasKey("temperature"))
        {
            NBTTagCompound temperatureCompound = tanData.getCompoundTag("temperature");

            temperatureLevel = temperatureCompound.getFloat("temperatureLevel");
            temperatureTimer = temperatureCompound.getInteger("temperatureTimer");
        }
    }

    @Override
    public void writeNBT(NBTTagCompound tanData)
    {
        NBTTagCompound temperatureCompound = new NBTTagCompound();
        
        temperatureCompound.setFloat("temperatureLevel", MathHelper.clamp_float(temperatureLevel, 27F, 47F));
        temperatureCompound.setInteger("temperatureTimer", temperatureTimer);
        
        tanData.setCompoundTag("temperature", temperatureCompound);
    }
    
    public static String getTemperatureSymbol()
    {
        String temperatureType = TANConfigurationTemperature.temperatureType;

        if (temperatureType.equals("Fahrenheit"))
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

        if (temperatureType.equals("Fahrenheit"))
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
}
