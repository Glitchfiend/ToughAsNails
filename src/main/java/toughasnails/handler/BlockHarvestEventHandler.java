package toughasnails.handler;

import java.util.Random;

import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.item.TANItems;
import toughasnails.item.ItemBark;
import toughasnails.item.ItemChunks;
import toughasnails.item.ItemPiles;
import toughasnails.item.ItemShards;

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
	        	event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), ItemChunks.ChunkType.STONE.ordinal()));
	        }
        	if (event.getState().getValue(BlockStone.VARIANT) == BlockStone.EnumType.ANDESITE)
	        {
	        	event.getDrops().clear();
	        	event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), ItemChunks.ChunkType.ANDESITE.ordinal()));
	        }
        	if (event.getState().getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE)
	        {
	        	event.getDrops().clear();
	        	event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), ItemChunks.ChunkType.DIORITE.ordinal()));
	        }
        	if (event.getState().getValue(BlockStone.VARIANT) == BlockStone.EnumType.GRANITE)
	        {
	        	event.getDrops().clear();
	        	event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), ItemChunks.ChunkType.GRANITE.ordinal()));
	        }
        }
        
        if (event.getState().getBlock() == Blocks.LOG && event.getHarvester() != null && !event.isSilkTouching())
        {
        	if (event.getState().getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.OAK)
	        {
	        	event.getDrops().clear();
	        	event.getDrops().add(new ItemStack(TANItems.bark, 2 + new Random().nextInt(2), ItemBark.BarkType.OAK.ordinal()));
	        }
        	if (event.getState().getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.SPRUCE)
	        {
	        	event.getDrops().clear();
	        	event.getDrops().add(new ItemStack(TANItems.bark, 2 + new Random().nextInt(2), ItemBark.BarkType.SPRUCE.ordinal()));
	        }
        	if (event.getState().getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.BIRCH)
	        {
	        	event.getDrops().clear();
	        	event.getDrops().add(new ItemStack(TANItems.bark, 2 + new Random().nextInt(2), ItemBark.BarkType.BIRCH.ordinal()));
	        }
        	if (event.getState().getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE)
	        {
	        	event.getDrops().clear();
	        	event.getDrops().add(new ItemStack(TANItems.bark, 2 + new Random().nextInt(2), ItemBark.BarkType.JUNGLE.ordinal()));
	        }
        }
        if (event.getState().getBlock() == Blocks.LOG2 && event.getHarvester() != null && !event.isSilkTouching())
        {
        	if (event.getState().getValue(BlockNewLog.VARIANT) == BlockPlanks.EnumType.ACACIA)
	        {
	        	event.getDrops().clear();
	            event.getDrops().add(new ItemStack(TANItems.bark, 2 + new Random().nextInt(2), ItemBark.BarkType.ACACIA.ordinal()));
	        }
        	if (event.getState().getValue(BlockNewLog.VARIANT) == BlockPlanks.EnumType.DARK_OAK)
	        {
	        	event.getDrops().clear();
	        	event.getDrops().add(new ItemStack(TANItems.bark, 2 + new Random().nextInt(2), ItemBark.BarkType.DARK_OAK.ordinal()));
	        }
        }
        
        if (event.getState().getBlock() == Blocks.GRASS && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.pile, 2 + new Random().nextInt(2), ItemPiles.PileType.DIRT.ordinal()));
        }
        
        if (event.getState().getBlock() == Blocks.GRASS_PATH && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.pile, 2 + new Random().nextInt(2), ItemPiles.PileType.DIRT.ordinal()));
        }
        
        if (event.getState().getBlock() == Blocks.DIRT && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.pile, 2 + new Random().nextInt(2), ItemPiles.PileType.DIRT.ordinal()));
        }
        
        if (event.getState().getBlock() == Blocks.GRAVEL && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.pile, 2 + new Random().nextInt(2), ItemPiles.PileType.GRAVEL.ordinal()));
	        
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
	        	event.getDrops().add(new ItemStack(TANItems.pile, 2 + new Random().nextInt(2), ItemPiles.PileType.SAND.ordinal()));
	        }
        	if (event.getState().getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND)
	        {
	        	event.getDrops().clear();
	        	event.getDrops().add(new ItemStack(TANItems.pile, 2 + new Random().nextInt(2), ItemPiles.PileType.RED_SAND.ordinal()));
	        }
        }
        
        if (event.getState().getBlock() == Blocks.OBSIDIAN && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.shard, 2 + new Random().nextInt(2), ItemShards.ShardType.OBSIDIAN.ordinal()));
        }
        
        if (event.getState().getBlock() == Blocks.NETHERRACK && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), ItemChunks.ChunkType.NETHERRACK.ordinal()));
        }
        
        if (event.getState().getBlock() == Blocks.END_STONE && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), ItemChunks.ChunkType.END_STONE.ordinal()));
        }
        
        if (event.getState().getBlock() == Blocks.IRON_ORE && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), ItemChunks.ChunkType.IRON_ORE.ordinal()));
        }
        
        if (event.getState().getBlock() == Blocks.GOLD_ORE && event.getHarvester() != null && !event.isSilkTouching())
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), ItemChunks.ChunkType.GOLD_ORE.ordinal()));
        }
    }
}
