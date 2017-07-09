package toughasnails.init;

import static toughasnails.api.TANBlocks.campfire;
import static toughasnails.api.TANBlocks.dead_crops;
import static toughasnails.api.TANBlocks.glowstone_torch;
import static toughasnails.api.TANBlocks.rain_collector;
import static toughasnails.api.TANBlocks.season_sensors;
import static toughasnails.api.TANBlocks.temperature_coil;
import static toughasnails.api.TANBlocks.torch_new;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import toughasnails.api.ITANBlock;
import toughasnails.block.BlockGlowstoneTorch;
import toughasnails.block.BlockRainCollector;
import toughasnails.block.BlockSeasonSensor;
import toughasnails.block.BlockSeasonSensor.DetectorType;
import toughasnails.block.BlockTANCampfire;
import toughasnails.block.BlockTANDeadCrops;
import toughasnails.block.BlockTANTemperatureCoil;
import toughasnails.block.BlockTANTorchNew;
import toughasnails.core.ToughAsNails;
import toughasnails.tileentity.TileEntitySeasonSensor;
import toughasnails.tileentity.TileEntityTemperatureSpread;
import toughasnails.util.BlockStateUtils;
import toughasnails.util.inventory.CreativeTabTAN;

public class ModBlocks
{
    
    public static void init()
    {
        season_sensors[0] = registerBlock( new BlockSeasonSensor(DetectorType.SPRING), "season_sensor_spring" );
        season_sensors[1] = registerBlock( new BlockSeasonSensor(DetectorType.SUMMER), "season_sensor_summer" ).setCreativeTab(null);
        season_sensors[2] = registerBlock( new BlockSeasonSensor(DetectorType.AUTUMN), "season_sensor_autumn" ).setCreativeTab(null);
        season_sensors[3] = registerBlock( new BlockSeasonSensor(DetectorType.WINTER), "season_sensor_winter" ).setCreativeTab(null); 
        campfire = registerBlock( new BlockTANCampfire(), "campfire" );
        rain_collector = registerBlock( new BlockRainCollector(), "rain_collector" );
        //gas = registerBlock( new BlockTANGas(), "gas" );
        //gas.setCreativeTab(null);
        temperature_coil = registerBlock(new BlockTANTemperatureCoil(), "temperature_coil");
        torch_new = registerBlock( new BlockTANTorchNew(), "torch_new" );
        glowstone_torch = registerBlock( new BlockGlowstoneTorch(), "glowstone_torch" );
        dead_crops = registerBlock(new BlockTANDeadCrops(), "dead_crops").setCreativeTab(null);  
        
        GameRegistry.registerTileEntity(TileEntityTemperatureSpread.class, "temperature_spread");
        GameRegistry.registerTileEntity(TileEntitySeasonSensor.class, "season_sensor");
    }
    
    
    public static void registerBlockVariant(Block block, String stateName, int stateMeta)
    {
        Item item = Item.getItemFromBlock(block);
        ToughAsNails.proxy.registerItemVariantModel(item, stateName, stateMeta);
    }
    
    public static Block registerBlock(Block block, String blockName)
    {
        // by default, set the creative tab for all blocks added in TAN to CreativeTabTAN.instance
        return registerBlock(block, blockName, CreativeTabTAN.instance);
    }
    
    public static Block registerBlock(Block block, String blockName, CreativeTabs tab)
    {

        block.setUnlocalizedName(blockName);        
        block.setCreativeTab(tab);
        
        if (block instanceof ITANBlock)
        {
            // if this block supports the IBOPBlock interface then we can determine the item block class, and sub-blocks automatically
            ITANBlock bopBlock = (ITANBlock)block;
            registerBlockWithItem(block, blockName, bopBlock.getItemClass());
            
            ToughAsNails.proxy.registerNonRenderingProperties(block);
            
            // check for missing default states
            IBlockState defaultState = block.getDefaultState();
            if (defaultState == null)
            {
                defaultState = block.getBlockState().getBaseState();
                ToughAsNails.logger.error("missing default state for " + block.getUnlocalizedName());
            }
            
            // get the preset blocks variants
            ImmutableSet<IBlockState> presets = BlockStateUtils.getBlockPresets(block);
            if (presets.isEmpty())
            {
                // block has no sub-blocks to register
                registerBlockVariant(block, blockName, 0);
            }
            else
            {
                // register all the sub-blocks
                for (IBlockState state : presets)
                {
                    String stateName = bopBlock.getStateName(state);
                    int stateMeta = block.getMetaFromState(state);
                    registerBlockVariant(block, stateName, stateMeta);
                }
            }
        }
        else
        {
            // for vanilla blocks, just register a single variant with meta=0 and assume ItemBlock for the item class
            registerBlockWithItem(block, blockName, ItemBlock.class);
            registerBlockVariant(block, blockName, 0);
        }

        return block;
    }
    
    private static void registerBlockWithItem(Block block, String blockName, Class<? extends ItemBlock> clazz)
    {
        try
        {
            Item itemBlock = clazz != null ? (Item)clazz.getConstructor(Block.class).newInstance(block) : null;
            ResourceLocation location = new ResourceLocation(ToughAsNails.MOD_ID, blockName);

            GameRegistry.register(block, location);
            if (itemBlock != null) GameRegistry.register(itemBlock, location);
        }
        catch (Exception e)
        {
            throw new RuntimeException("An error occurred associating an item block during registration...");
        }
    }
}
