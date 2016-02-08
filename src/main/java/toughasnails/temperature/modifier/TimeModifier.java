package toughasnails.temperature.modifier;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureInfo;

public class TimeModifier extends TemperatureModifier
{
    public static final int TIME_TARGET_MODIFIER = 10;
    
    /**Multiplies how much should the temperature be increased/decreased by the closer the
     * biome temp is to a extreme hot or cold
     */
    public static final float EXTREMITY_MULTIPLIER = 2.0F;
    
    private Random dayRandom = new Random();
    
    public TimeModifier(TemperatureDebugger debugger)
    {
        super(debugger);
    }
    
    @Override
    public int modifyChangeRate(World world, EntityPlayer player, int changeRate)
    {
        return changeRate;
    }

    @Override
    public TemperatureInfo modifyTarget(World world, EntityPlayer player, TemperatureInfo temperature)
    {
        BiomeGenBase biome = world.getBiomeGenForCoords(player.getPosition());
        long worldTime = world.getWorldTime();
        
        //The biome temperature on a scale of 0 to 1, 0 freezing and 1 boiling hot
        float biomeTempNorm = (MathHelper.clamp_float(biome.temperature, -0.5F, 2.0F) + 0.5F) / 2.5F;
        //The biome temperature from 0 to 1, 0 least extreme and 1 most extreme
        float extremityModifier = Math.abs(biomeTempNorm * 2.0F - 1.0F);
        //Reaches the highest point during the middle of the day and at midnight. Normalized to be between -1 and 1
        float timeNorm = (-Math.abs(((worldTime + 6000) % 24000.0F) - 12000.0F) + 6000.0F) / 6000.0F;
        
        int temperatureLevel = temperature.getScalePos();
        int newTemperatureLevel = temperatureLevel;

        debugger.start(Modifier.TIME_TARGET, newTemperatureLevel);
        newTemperatureLevel += TIME_TARGET_MODIFIER * timeNorm * (Math.max(1.0F, extremityModifier * EXTREMITY_MULTIPLIER));
        debugger.end(newTemperatureLevel);
        
        dayRandom.setSeed((int)(worldTime / 24000));
        
        debugger.start(Modifier.DAY_RANDOM_TARGET, newTemperatureLevel);
        newTemperatureLevel += dayRandom.nextInt(10) - 5;
        debugger.end(newTemperatureLevel);
        
        return new TemperatureInfo(newTemperatureLevel);
    }
}
