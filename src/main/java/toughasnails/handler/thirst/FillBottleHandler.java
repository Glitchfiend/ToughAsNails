/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.thirst;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.item.TANItems;

public class FillBottleHandler 
{
    @SubscribeEvent
    public void onPlayerRightClickWater(PlayerInteractEvent.RightClickItem event)
    {
        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();
        
        if (stack.getItem() == Items.glass_bottle)
        {
            //Provide an itemstack with a size of 0 so no water bottles get added to the inventory if successful
            EnumActionResult actionResult = stack.getItem().onItemRightClick(new ItemStack(stack.getItem(), 0), event.getWorld(), player, event.getHand()).getType();
        
            if (actionResult == EnumActionResult.SUCCESS)
            {
                --stack.stackSize;
                player.addStat(StatList.getObjectUseStats(stack.getItem()));
                ItemStack bottleStack = new ItemStack(TANItems.water_bottle);
                
                if (!player.inventory.addItemStackToInventory(bottleStack))
                {
                    player.dropPlayerItemWithRandomChoice(bottleStack, false);
                }

                event.setCanceled(true);
            }
        }
    }
}
