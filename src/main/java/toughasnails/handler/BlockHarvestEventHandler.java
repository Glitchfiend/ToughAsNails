package toughasnails.handler;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.item.TANItems;

public class BlockHarvestEventHandler {
	@SubscribeEvent
	public void onBlockBreak(HarvestDropsEvent event) {
		IBlockState state = event.getState();
		// int fortune = event.getFortuneLevel();

		if (event.getHarvester() == null || event.isSilkTouching())
			return;

		if (state.getBlock() == Blocks.ICE && event.getHarvester() != null) {
			event.getDrops().clear();
			event.getDrops().add(
					new ItemStack(TANItems.ice_cube, new Random().nextInt(2)));
		}
	}
}
