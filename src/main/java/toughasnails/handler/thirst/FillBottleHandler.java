/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.thirst;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.item.TANItems;
import toughasnails.api.config.GameplayOption;

public class FillBottleHandler 
{
    @SubscribeEvent
    public void onPlayerRightClickWater(PlayerInteractEvent.RightClickItem event)
    {
        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();
        
        if (stack.getItem() == Items.GLASS_BOTTLE && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST))
        {
            //Provide an itemstack with a size of 0 so no water bottles get added to the inventory if successful
            EnumActionResult actionResult = stack.getItem().onItemRightClick(event.getWorld(), player, event.getHand()).getType();
        
            if (actionResult == EnumActionResult.SUCCESS)
            {
                stack.setCount(stack.getCount() - 1);
                player.addStat(StatList.getObjectUseStats(stack.getItem()));
                ItemStack bottleStack = new ItemStack(TANItems.water_bottle);
                
                if (!player.inventory.addItemStackToInventory(bottleStack))
                {
                    player.dropItem(bottleStack, false);
                }

                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public void onRightClickCauldron(PlayerInteractEvent.RightClickBlock event)
    {
        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();
        IBlockState state = world.getBlockState(event.getPos());
        
        if (state.getBlock() instanceof BlockCauldron && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST))
        {
            BlockCauldron cauldron = (BlockCauldron)state.getBlock();
            int level = ((Integer)state.getValue(BlockCauldron.LEVEL));

            if (level > 0 && !world.isRemote)
            {
                if (!player.capabilities.isCreativeMode)
                {
                    ItemStack waterBottle = new ItemStack(TANItems.water_bottle);
                    player.addStat(StatList.CAULDRON_USED);
                    player.getHeldItem(event.getHand()).setCount(player.getHeldItem(event.getHand()).getCount() - 1);
                    if (player.getHeldItem(event.getHand()).isEmpty())
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
                event.setCanceled(true);
            }
        }
    }
}
