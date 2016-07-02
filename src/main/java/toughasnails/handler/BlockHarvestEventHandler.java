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
import toughasnails.item.ItemBark;
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
        else if (state.getBlock() == Blocks.STONE)
        {
            int meta;
            
            switch ((BlockStone.EnumType)state.getValue(BlockStone.VARIANT))
            {
            case STONE:
                meta = 0;
                break;
                
            case ANDESITE:
                meta = 1;
                break;
                
            case DIORITE:
                meta = 2;
                break;
                
            case GRANITE:
                meta = 3;
                break;
                
            default:
                return; //Don't touch other variants
            }
            
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), meta));
        }
        else if (state.getBlock() == Blocks.LOG)
        {
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(TANItems.bark, 2 + new Random().nextInt(2), state.getValue(BlockOldLog.VARIANT).ordinal()));
        }
        else if (state.getBlock() == Blocks.LOG2)
        {
        	if (state.getValue(BlockNewLog.VARIANT) == BlockPlanks.EnumType.ACACIA)
	        {
	        	event.getDrops().clear();
	            event.getDrops().add(new ItemStack(TANItems.bark, 2 + new Random().nextInt(2), ItemBark.BarkType.ACACIA.ordinal()));
	        }
        	if (state.getValue(BlockNewLog.VARIANT) == BlockPlanks.EnumType.DARK_OAK)
	        {
	        	event.getDrops().clear();
	        	event.getDrops().add(new ItemStack(TANItems.bark, 2 + new Random().nextInt(2), ItemBark.BarkType.DARK_OAK.ordinal()));
	        }
        }
        else if (state.getBlock() == Blocks.GRASS)
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.pile, 2 + new Random().nextInt(2), ItemPiles.PileType.DIRT.ordinal()));
        }
        else if (state.getBlock() == Blocks.GRASS_PATH)
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.pile, 2 + new Random().nextInt(2), ItemPiles.PileType.DIRT.ordinal()));
        }
        else if (state.getBlock() == Blocks.DIRT)
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.pile, 2 + new Random().nextInt(2), ItemPiles.PileType.DIRT.ordinal()));
        }
        else if (state.getBlock() == Blocks.GRAVEL)
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
        else if (state.getBlock() == Blocks.SAND)
        {
        	if (state.getValue(BlockSand.VARIANT) == BlockSand.EnumType.SAND)
	        {
	        	event.getDrops().clear();
	        	event.getDrops().add(new ItemStack(TANItems.pile, 2 + new Random().nextInt(2), ItemPiles.PileType.SAND.ordinal()));
	        }
        	if (state.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND)
	        {
	        	event.getDrops().clear();
	        	event.getDrops().add(new ItemStack(TANItems.pile, 2 + new Random().nextInt(2), ItemPiles.PileType.RED_SAND.ordinal()));
	        }
        }
        else if (state.getBlock() == Blocks.OBSIDIAN)
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.shard, 2 + new Random().nextInt(2), ItemShards.ShardType.OBSIDIAN.ordinal()));
        }
        else if (state.getBlock() == Blocks.NETHERRACK)
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), ItemChunks.ChunkType.NETHERRACK.ordinal()));
        }
        else if (state.getBlock() == Blocks.END_STONE)
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), ItemChunks.ChunkType.END_STONE.ordinal()));
        }
        else if (state.getBlock() == Blocks.IRON_ORE)
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), ItemChunks.ChunkType.IRON_ORE.ordinal()));
        }
        else if (state.getBlock() == Blocks.GOLD_ORE)
        {
	        event.getDrops().clear();
	        event.getDrops().add(new ItemStack(TANItems.chunk, 2 + new Random().nextInt(2), ItemChunks.ChunkType.GOLD_ORE.ordinal()));
        }
    }
}
