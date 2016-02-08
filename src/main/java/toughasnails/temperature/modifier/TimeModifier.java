package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureInfo;
import toughasnails.util.BiomeUtils;

public class TimeModifier extends TemperatureModifier
{
    public static final int TIME_TARGET_MODIFIER = 5;
    
    /**
     * Multiplies how much should the temperature be increased/decreased by the closer the
     * biome temp is to a extreme hot or cold
     */
    public static final float EXTREMITY_MULTIPLIER = 2.0F;
    
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
        
        float extremityModifier = BiomeUtils.getBiomeTempExtremity(biome);
        //Reaches the highest point during the middle of the day and at midnight. Normalized to be between -1 and 1
        float timeNorm = (-Math.abs(((worldTime + 6000) % 24000.0F) - 12000.0F) + 6000.0F) / 6000.0F;
        
        int temperatureLevel = temperature.getScalePos();
        int newTemperatureLevel = temperatureLevel;

        debugger.start(Modifier.TIME_TARGET, newTemperatureLevel);
        newTemperatureLevel += TIME_TARGET_MODIFIER * timeNorm * (Math.max(1.0F, extremityModifier * EXTREMITY_MULTIPLIER));
        debugger.end(newTemperatureLevel);
        
        return new TemperatureInfo(newTemperatureLevel);
    }
}
