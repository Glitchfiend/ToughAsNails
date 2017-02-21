/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.thirst;

import java.util.Set;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.item.TANItems;

public class FillBottleHandler 
{
    /**
     * Substitutes normal filled water bottles for dirty water bottles
     */
    @SubscribeEvent
    public void onPlayerRightClickWater(PlayerInteractEvent.RightClickItem event) throws Exception
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItem(event.getHand());
        World world = player.worldObj;

        if (stack != null && stack.getItem().equals(Items.GLASS_BOTTLE) && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST))
        {
        	
            ActionResult actionResult = stack.getItem().onItemRightClick(new ItemStack(stack.getItem(), 0), event.getWorld(), player, event.getHand());
			ItemStack resultStack = ((ItemStack)actionResult.getResult());
		
			//NOTES FROM 1.9.4 testing.  ResultStack is item.potion only when used on water. Dragon's breath is different.
            //Don't compare tags directly, the water bottle in resultStack doesn't have Potion tag.
            if (actionResult.getType().equals(EnumActionResult.SUCCESS) && resultStack != null && ItemStack.areItemsEqual(resultStack, new ItemStack(Items.POTIONITEM)))
            {
              
                --stack.stackSize; // Restore original amount of bottles
                player.addStat(StatList.getObjectUseStats(stack.getItem()));
                ItemStack bottleStack = new ItemStack(TANItems.water_bottle);

                if (!player.inventory.addItemStackToInventory(bottleStack))
                {
                    player.dropItem(bottleStack, false);
                }
				 event.setCanceled(true);

                // Prevent onItemRightClick from being fired a second time for bottles right clicked on water
            

            }
           
        }
    }

    /**
     * Produce dirty water bottles when filling empty bottles from the cauldron
     */
    @SubscribeEvent
    public void onRightClickCauldron(PlayerInteractEvent.RightClickBlock event)
    {
        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();
        IBlockState state = world.getBlockState(event.getPos());
        ItemStack heldStack = player.getHeldItem(event.getHand());
        Item heldItem;
        if (heldStack == null || state == null || (heldItem = heldStack.getItem()) == null) {
        	event.setCanceled(true);
        	return;
        }
        
        
        if (state.getBlock() instanceof BlockCauldron && heldItem == Items.GLASS_BOTTLE && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST))
        {
            BlockCauldron cauldron = (BlockCauldron)state.getBlock();
            int level = ((Integer)state.getValue(BlockCauldron.LEVEL));

            // Only fill when the cauldron has water in it
            if (level > 0 && !world.isRemote)
            {
                if (!player.capabilities.isCreativeMode)
                {
                    ItemStack waterBottle = new ItemStack(TANItems.water_bottle);
                    player.addStat(StatList.CAULDRON_USED);

                    if (--player.getHeldItem(event.getHand()).stackSize == 0)

                    {
                        player.setHeldItem(event.getHand(), waterBottle);
                    }
                    else if (!player.inventory.addItemStackToInventory(waterBottle))
                    {
                        player.dropItem(waterBottle, false);
                    }
                    else if (player instanceof EntityPlayerMP)
                    {
                        ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                    }
                }

                cauldron.setWaterLevel(world, event.getPos(), state, level - 1);
                // Prevent from producing a Vanilla water bottle
                event.setCanceled(true);
            }
        }
    }
}
