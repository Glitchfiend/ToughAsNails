package toughasnails.init;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.handler.ExtendedStatHandler;
import toughasnails.handler.MaxHealthHandler;
import toughasnails.handler.PacketHandler;
import toughasnails.handler.TemperatureDebugOverlayHandler;
import toughasnails.handler.TemperatureOverlayHandler;
import toughasnails.handler.ThirstOverlayHandler;
import toughasnails.handler.ThirstStatHandler;
import toughasnails.handler.season.ProviderIceHandler;
import toughasnails.handler.season.RandomUpdateHandler;
import toughasnails.handler.season.SeasonHandler;
import toughasnails.handler.season.SeasonSleepHandler;
import toughasnails.handler.season.StopSpawnHandler;
import toughasnails.handler.season.WeatherFrequencyHandler;
import toughasnails.season.SeasonTime;
import toughasnails.util.SeasonColourUtil;

public class ModHandlers
{
    public static void init()
    {
        PacketHandler.init();

        ExtendedStatHandler extendedStatHandler = new ExtendedStatHandler();
        
        FMLCommonHandler.instance().bus().register(extendedStatHandler);
        MinecraftForge.EVENT_BUS.register(new ExtendedStatHandler());
        
        TemperatureOverlayHandler temperatureOverlayHandler = new TemperatureOverlayHandler();

        FMLCommonHandler.instance().bus().register(temperatureOverlayHandler);
        MinecraftForge.EVENT_BUS.register(temperatureOverlayHandler);
        MinecraftForge.EVENT_BUS.register(new TemperatureDebugOverlayHandler());
        
        ThirstOverlayHandler thirstOverlayHandler = new ThirstOverlayHandler();
        ThirstStatHandler thirstStatHandler = new ThirstStatHandler();
        
        FMLCommonHandler.instance().bus().register(thirstOverlayHandler);
        MinecraftForge.EVENT_BUS.register(thirstOverlayHandler);
        FMLCommonHandler.instance().bus().register(thirstStatHandler);
        MinecraftForge.EVENT_BUS.register(thirstStatHandler);
        
        MinecraftForge.EVENT_BUS.register(new MaxHealthHandler());
        
        //Handlers for functionality related to seasons
        MinecraftForge.EVENT_BUS.register(new SeasonHandler());
        MinecraftForge.EVENT_BUS.register(new RandomUpdateHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(new ProviderIceHandler());
        MinecraftForge.EVENT_BUS.register(new SeasonSleepHandler());
        StopSpawnHandler stopSpawnHandler = new StopSpawnHandler();
        MinecraftForge.EVENT_BUS.register(stopSpawnHandler);
        MinecraftForge.TERRAIN_GEN_BUS.register(stopSpawnHandler);
        MinecraftForge.EVENT_BUS.register(new WeatherFrequencyHandler());
        
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            registerSeasonColourHandlers();
        }
    }
    
    @SideOnly(Side.CLIENT)
    private static void registerSeasonColourHandlers()
    {
        BiomeColorHelper.GRASS_COLOR = new BiomeColorHelper.ColorResolver()
        {
            @Override
            public int getColorAtPos(BiomeGenBase biome, BlockPos blockPosition)
            {
                SeasonTime calendar = new SeasonTime(SeasonHandler.clientSeasonCycleTicks);
                return SeasonColourUtil.applySeasonalGrassColouring(calendar.getSubSeason(), biome.getGrassColorAtPos(blockPosition));
            }
        };
        
        BiomeColorHelper.FOLIAGE_COLOR = new BiomeColorHelper.ColorResolver()
        {
            @Override
            public int getColorAtPos(BiomeGenBase biome, BlockPos blockPosition)
            {
                SeasonTime calendar = new SeasonTime(SeasonHandler.clientSeasonCycleTicks);
                return SeasonColourUtil.applySeasonalFoliageColouring(calendar.getSubSeason(), biome.getFoliageColorAtPos(blockPosition));
            }
        };
    }
}
