package toughasnails.handler;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.item.TANItems;
import toughasnails.init.ModConfig;

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
        	if (ModConfig.gameplay.iceCubeDrops)
        	{
	        	event.getDrops().clear();
	            event.getDrops().add(new ItemStack(TANItems.ice_cube, new Random().nextInt(fortune + 1) + new Random().nextInt(2)));
        	}
        }
        
        if (state.getBlock() == Blocks.MAGMA && event.getHarvester() != null)
        {
        	if (ModConfig.gameplay.magmaShardDrops)
        	{
	        	event.getDrops().clear();
	            event.getDrops().add(new ItemStack(TANItems.magma_shard, 1 + new Random().nextInt(fortune + 1) + new Random().nextInt(3)));
        	}
        }
    }
}
