package toughasnails.handler;

import java.util.Random;

import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.item.TANItems;

public class BlockHarvestEventHandler
{
	@SubscribeEvent
    public void onBlockBreak(HarvestDropsEvent event)
    {	
		int fortune = event.getFortuneLevel();
		
        if (event.getState().getBlock() == Blocks.ICE && event.getHarvester() != null)
        {
        	event.getDrops().clear();
            event.getDrops().add(new ItemStack(TANItems.ice_cube, new Random().nextInt(2)));
        }
        
        if (event.getState().getBlock() == Blocks.STONE && event.getHarvester() != null && !event.isSilkTouching())
        {
        	if (event.getState().getValue(BlockStone.VARIANT) == BlockStone.EnumType.STONE)
	        {
	        	event.getDrops().clear();
	            event.getDrops().add(new ItemStack(TANItems.stone_chunk, 2 + new Random().nextInt(2)));
	        }
        	if (event.getState().getValue(BlockStone.VARIANT) == BlockStone.EnumType.ANDESITE)
	        {
	        	event.getDrops().clear();
	            event.getDrops().add(new ItemStack(TANItems.stone_chunk_andesite, 2 + new Random().nextInt(2)));
	        }
        	if (event.getState().getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE)
	        {
	        	event.getDrops().clear();
	            event.getDrops().add(new ItemStack(TANItems.stone_chunk_diorite, 2 + new Random().nextInt(2)));
	        }
        	if (event.getState().getValue(BlockStone.VARIANT) == BlockStone.EnumType.GRANITE)
	        {
	        	event.getDrops().clear();
	            event.getDrops().add(new ItemStack(TANItems.stone_chunk_granite, 2 + new Random().nextInt(2)));
	        }
        }
        
        if (event.getState().getBlock() == Blocks.GRASS && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.pile_of_dirt, 2 + new Random().nextInt(2)));
        }
        
        if (event.getState().getBlock() == Blocks.GRASS_PATH && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.pile_of_dirt, 2 + new Random().nextInt(2)));
        }
        
        if (event.getState().getBlock() == Blocks.DIRT && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.pile_of_dirt, 2 + new Random().nextInt(2)));
        }
        
        if (event.getState().getBlock() == Blocks.GRAVEL && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.pile_of_gravel, 2 + new Random().nextInt(2)));
	        
	        if (fortune > 3)
	        {
	            fortune = 3;
	        }
	        
	        if (new Random().nextInt(10 - fortune * 3) == 0)
	        {
	        	event.getDrops().add(new ItemStack(Items.FLINT, 1));
	        }
        }
        
        if (event.getState().getBlock() == Blocks.SAND && event.getHarvester() != null && !event.isSilkTouching())
        {
        	if (event.getState().getValue(BlockSand.VARIANT) == BlockSand.EnumType.SAND)
	        {
	        	event.getDrops().clear();
	            event.getDrops().add(new ItemStack(TANItems.pile_of_sand, 2 + new Random().nextInt(2)));
	        }
        }
        
        if (event.getState().getBlock() == Blocks.OBSIDIAN && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.obsidian_shard, 2 + new Random().nextInt(2)));
        }
        
        if (event.getState().getBlock() == Blocks.NETHERRACK && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.netherrack_chunk, 2 + new Random().nextInt(2)));
        }
        
        if (event.getState().getBlock() == Blocks.END_STONE && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.end_stone_chunk, 2 + new Random().nextInt(2)));
        }
        
        if (event.getState().getBlock() == Blocks.IRON_ORE && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.iron_ore_chunk, 2 + new Random().nextInt(2)));
        }
        
        if (event.getState().getBlock() == Blocks.GOLD_ORE && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.gold_ore_chunk, 2 + new Random().nextInt(2)));
        }
    }
}
