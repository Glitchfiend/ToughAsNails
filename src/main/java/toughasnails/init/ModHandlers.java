package toughasnails.init;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.handler.ExtendedStatHandler;
import toughasnails.handler.PacketHandler;
import toughasnails.handler.SeasonHandler;
import toughasnails.handler.TemperatureDebugOverlayHandler;
import toughasnails.handler.TemperatureOverlayHandler;
import toughasnails.handler.ThirstOverlayHandler;
import toughasnails.handler.ThirstStatHandler;
import toughasnails.season.Calendar;
import toughasnails.util.ColourUtil;

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
        
        MinecraftForge.EVENT_BUS.register(new SeasonHandler());
    
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
                Calendar calendar = new Calendar(SeasonHandler.clientSeasonCycleTicks);
                int grass = biome.getGrassColorAtPos(blockPosition);
                int overlay = calendar.getSubSeason().getGrassColour();
                return overlay == 0xFFFFFF ? grass : ColourUtil.overlayBlend(grass, overlay);
            }
        };
        
        BiomeColorHelper.FOLIAGE_COLOR = new BiomeColorHelper.ColorResolver()
        {
            @Override
            public int getColorAtPos(BiomeGenBase biome, BlockPos blockPosition)
            {
                Calendar calendar = new Calendar(SeasonHandler.clientSeasonCycleTicks);
                int foliage = biome.getFoliageColorAtPos(blockPosition);
                int overlay = calendar.getSubSeason().getGrassColour();
                return overlay == 0xFFFFFF ? foliage : ColourUtil.overlayBlend(foliage, overlay);
            }
        };
    }
}
