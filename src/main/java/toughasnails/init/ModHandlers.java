package toughasnails.init;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SeasonsOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.SeasonHelper;
import toughasnails.config.SyncedConfigHandler;
import toughasnails.handler.AchievementEventHandler;
import toughasnails.handler.ExtendedStatHandler;
import toughasnails.handler.PacketHandler;
import toughasnails.handler.health.HealthOverlayHandler;
import toughasnails.handler.health.MaxHealthHandler;
import toughasnails.handler.season.ProviderIceHandler;
import toughasnails.handler.season.RandomUpdateHandler;
import toughasnails.handler.season.SeasonCropHandler;
import toughasnails.handler.season.SeasonHandler;
import toughasnails.handler.season.SeasonSleepHandler;
import toughasnails.handler.season.StopSpawnHandler;
import toughasnails.handler.season.WeatherFrequencyHandler;
import toughasnails.handler.temperature.TemperatureStatTableHandler;
import toughasnails.handler.temperature.TemperatureOverlayHandler;
import toughasnails.handler.thirst.FillBottleHandler;
import toughasnails.handler.thirst.ThirstOverlayHandler;
import toughasnails.handler.thirst.ThirstStatHandler;
import toughasnails.handler.thirst.VanillaDrinkHandler;
import toughasnails.season.SeasonTime;
import toughasnails.util.SeasonColourUtil;

public class ModHandlers
{
    private static final SeasonHandler SEASON_HANDLER = new SeasonHandler();

    public static void init()
    {
        PacketHandler.init();

        MinecraftForge.EVENT_BUS.register(new ExtendedStatHandler());
        MinecraftForge.EVENT_BUS.register(new SyncedConfigHandler());
        
        MinecraftForge.EVENT_BUS.register(new ThirstStatHandler());
        MinecraftForge.EVENT_BUS.register(new VanillaDrinkHandler());
	    MinecraftForge.EVENT_BUS.register(new FillBottleHandler());
	    MinecraftForge.EVENT_BUS.register(new MaxHealthHandler());

        //Handlers for functionality related to seasons
        MinecraftForge.EVENT_BUS.register(SEASON_HANDLER);
        SeasonHelper.dataProvider = SEASON_HANDLER;
        MinecraftForge.EVENT_BUS.register(new RandomUpdateHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(new ProviderIceHandler());
        MinecraftForge.EVENT_BUS.register(new SeasonCropHandler());
        MinecraftForge.EVENT_BUS.register(new SeasonSleepHandler());
        
        if (!(ModConfig.seasons.winterAnimalSpawns))
            {
            StopSpawnHandler stopSpawnHandler = new StopSpawnHandler();
            MinecraftForge.EVENT_BUS.register(stopSpawnHandler);
            MinecraftForge.TERRAIN_GEN_BUS.register(stopSpawnHandler);
            }
        
        MinecraftForge.EVENT_BUS.register(new WeatherFrequencyHandler());
        MinecraftForge.EVENT_BUS.register(new AchievementEventHandler());
        
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            MinecraftForge.EVENT_BUS.register(new TemperatureOverlayHandler());
            MinecraftForge.EVENT_BUS.register(new TemperatureStatTableHandler());
            MinecraftForge.EVENT_BUS.register(new ThirstOverlayHandler());
            MinecraftForge.EVENT_BUS.register(new HealthOverlayHandler());

            registerSeasonColourHandlers();
        }
    }
    
    @SideOnly(Side.CLIENT)
    private static void registerSeasonColourHandlers()
    {
        BiomeColorHelper.GRASS_COLOR = new BiomeColorHelper.ColorResolver()
        {
            @Override
            public int getColorAtPos(Biome biome, BlockPos blockPosition)
            {
                SeasonTime calendar = new SeasonTime(SeasonHandler.clientSeasonCycleTicks);
                return SeasonColourUtil.applySeasonalGrassColouring(calendar.getSubSeason(), biome.getGrassColorAtPos(blockPosition));
            }
        };
        
        BiomeColorHelper.FOLIAGE_COLOR = new BiomeColorHelper.ColorResolver()
        {
            @Override
            public int getColorAtPos(Biome biome, BlockPos blockPosition)
            {
                SeasonTime calendar = new SeasonTime(SeasonHandler.clientSeasonCycleTicks);
                return SeasonColourUtil.applySeasonalFoliageColouring(calendar.getSubSeason(), biome.getFoliageColorAtPos(blockPosition));
            }
        };
    }
}
