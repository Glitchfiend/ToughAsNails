package toughasnails.handler;

import java.util.Random;

import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.item.TANItems;
import toughasnails.item.ItemSplit;
import toughasnails.item.ItemChunks;
import toughasnails.item.ItemPiles;
import toughasnails.item.ItemShards;

public class BlockHarvestEventHandler
{
	@SubscribeEvent
    public void onBlockBreak(HarvestDropsEvent event)
    {	
	    IBlockState state = event.getState();
		int fortune = event.getFortuneLevel();
		
		if (event.getHarvester() == null || event.isSilkTouching())
		    return;
		
        if (state.getBlock() == Blocks.ICE && event.getHarvester() != null)
        {
        	event.getDrops().clear();
            event.getDrops().add(new ItemStack(TANItems.ice_cube, new Random().nextInt(2)));
        }
    }
}
