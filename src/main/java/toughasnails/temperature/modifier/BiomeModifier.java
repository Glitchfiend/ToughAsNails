package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.temperature.Temperature;
import toughasnails.init.ModConfig;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.util.BiomeUtils;

public class BiomeModifier extends TemperatureModifier
{
    public static final int MAX_TEMP_OFFSET = 10;
    
    public BiomeModifier(TemperatureDebugger debugger)
    {
        super(debugger);
    }

    @Override
    public Temperature applyEnvironmentModifiers(World world, BlockPos pos, Temperature initialTemperature)
    {
        Biome biome = world.getBiome(pos);
        Biome biomeNorth = world.getBiome(pos.add(0, 0, -10));
        Biome biomeSouth = world.getBiome(pos.add(0, 0, 10));
        Biome biomeEast = world.getBiome(pos.add(10, 0, 0));
        Biome biomeWest = world.getBiome(pos.add(-10, 0, 0));
        
        float biomeTemp = ((BiomeUtils.getBiomeTempNorm(biome) + BiomeUtils.getBiomeTempNorm(biomeNorth) + BiomeUtils.getBiomeTempNorm(biomeSouth) + BiomeUtils.getBiomeTempNorm(biomeEast) + BiomeUtils.getBiomeTempNorm(biomeWest)) / 5.0F);
        
        //Denormalize, multiply by the max temp offset, add to the current temp
        int newTemperatureLevel = initialTemperature.getRawValue() + (int)Math.round((biomeTemp * 2.0F - 1.0F) * ModConfig.temperature.maxBiomeTempOffset);
        
        debugger.start(Modifier.BIOME_TEMPERATURE_TARGET, initialTemperature.getRawValue());
        debugger.end(newTemperatureLevel);
        
        return new Temperature(newTemperatureLevel);
    }

    @Override
    public boolean isPlayerSpecific()
    {
        return false;
    }
}
