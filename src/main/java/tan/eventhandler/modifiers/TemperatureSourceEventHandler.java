package tan.eventhandler.modifiers;

import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import tan.api.event.temperature.TemperatureEvent;
import tan.api.temperature.TemperatureRegistry;

public class TemperatureSourceEventHandler
{
    @ForgeSubscribe
    public void modifyTemperature(TemperatureEvent event)
    {
        ArrayList<Float> temperatureModifiers = new ArrayList<Float>();
        
        EntityPlayer player = event.player;
        World world = player.worldObj;
        
        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);
        
        for (int ix = -2; ix <= 2; ix++)
        {
            for (int iy = -1; iy <= 1; iy++)
            {
                for (int iz = -2; iz <= 2; iz++)
                {
                    int blockID = world.getBlockId(x + ix, y + iy, z + iz);
                    int metadata = world.getBlockMetadata(x + ix, y + iy, z + iz);

                    float temperatureModifier = TemperatureRegistry.getTemperatureSourceModifier(blockID, metadata);

                    temperatureModifiers.add(temperatureModifier);
                }
            }
        }
        
        float total = 0F;
        int divider = 0;
        
        for (float temperatureModifier : temperatureModifiers)
        {
            total += temperatureModifier;
            divider++;
        }
        
        if ((total / divider) > 0)
        {
            event.temperature += Collections.max(temperatureModifiers);
        }
        else
        {
            event.temperature += Collections.min(temperatureModifiers);
        }
    }
    
    @ForgeSubscribe
    public void modifyRate(TemperatureEvent.Rate event)
    {
        ArrayList<Float> rateModifiers = new ArrayList<Float>();
        
        EntityPlayer player = event.player;
        World world = player.worldObj;
        
        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);
        
        for (int ix = -2; ix <= 2; ix++)
        {
            for (int iy = -1; iy <= 1; iy++)
            {
                for (int iz = -2; iz <= 2; iz++)
                {
                    int blockID = world.getBlockId(x + ix, y + iy, z + iz);
                    int metadata = world.getBlockMetadata(x + ix, y + iy, z + iz);

                    float rateModifier = TemperatureRegistry.getTemperatureSourceRate(blockID, metadata);

                    rateModifiers.add(rateModifier);
                }
            }
        }
        
        float total = 0F;
        int divider = 0;
        
        for (float rateModifier : rateModifiers)
        {
            total += rateModifier;
            divider++;
        }
        
        if ((total / divider) > 0)
        {
            event.rate += Collections.max(rateModifiers);
        }
        else
        {
            event.rate += Collections.min(rateModifiers);
        }
    }
}
