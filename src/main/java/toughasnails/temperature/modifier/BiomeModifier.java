package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.temperature.Temperature;
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
    public Temperature modifyTarget(World world, EntityPlayer player, Temperature temperature)
    {
        Biome biome = world.getBiome(player.getPosition());
        Biome biomeNorth = world.getBiome(player.getPosition().add(0, 0, -10));
        Biome biomeSouth = world.getBiome(player.getPosition().add(0, 0, 10));
        Biome biomeEast = world.getBiome(player.getPosition().add(10, 0, 0));
        Biome biomeWest = world.getBiome(player.getPosition().add(-10, 0, 0));
        
        float biomeTemp = ((BiomeUtils.getBiomeTempNorm(biome) + BiomeUtils.getBiomeTempNorm(biomeNorth) + BiomeUtils.getBiomeTempNorm(biomeSouth) + BiomeUtils.getBiomeTempNorm(biomeEast) + BiomeUtils.getBiomeTempNorm(biomeWest)) / 5.0F);
        
        //Denormalize, multiply by the max temp offset, add to the current temp
        int newTemperatureLevel = temperature.getRawValue() + (int)Math.round((biomeTemp * 2.0F - 1.0F) * MAX_TEMP_OFFSET);
        
        debugger.start(Modifier.BIOME_TEMPERATURE_TARGET, temperature.getRawValue());
        debugger.end(newTemperatureLevel);
        
        return new Temperature(newTemperatureLevel);
    }
}
