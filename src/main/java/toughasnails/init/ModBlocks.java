package toughasnails.init;

import static toughasnails.api.TANBlocks.campfire;
import static toughasnails.api.TANBlocks.gas;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import toughasnails.api.ITANBlock;
import toughasnails.block.BlockTANCampfire;
import toughasnails.block.BlockTANGas;
import toughasnails.core.ToughAsNails;
import toughasnails.util.BlockStateUtils;
import toughasnails.util.inventory.CreativeTabTAN;

public class ModBlocks
{
    
    public static void init()
    {
        campfire = registerBlock( new BlockTANCampfire(), "campfire" );
        gas = registerBlock( new BlockTANGas(), "gas" );
        gas.setCreativeTab(null);
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
            GameRegistry.registerBlock(block, bopBlock.getItemClass(), blockName);
            
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
            GameRegistry.registerBlock(block, ItemBlock.class , blockName);
            registerBlockVariant(block, blockName, 0);
        }

        return block;
    }
    
}
