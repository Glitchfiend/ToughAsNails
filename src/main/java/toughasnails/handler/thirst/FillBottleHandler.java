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
import net.minecraft.init.PotionTypes;
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

        if (stack.getItem().equals(Items.GLASS_BOTTLE) && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST))
        {

            int originalCount = stack.stackSize;
            // Trick onItemRightClick into not adding any water bottles into the player's inventory
            stack.stackSize = 1;

            ActionResult actionResult = stack.getItem().onItemRightClick(new ItemStack(stack.getItem(), 0), event.getWorld(), player, event.getHand());
            ItemStack resultStack = ((ItemStack)actionResult.getResult());

            // Only substitute water bottles with dirty water bottles
            if (actionResult.getType() == EnumActionResult.SUCCESS && ItemStack.areItemStackTagsEqual(resultStack, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER)))
            {
                // We must restore the original amount of bottles before continuing to prevent the fake empty bottle
                // stack from being replaced

                // A bottle has been consumed, so reduce the original count by one before it is restored
                originalCount--;
                stack.stackSize = originalCount; // Restore original amount of bottles
                player.addStat(StatList.getObjectUseStats(stack.getItem()));
                ItemStack bottleStack = new ItemStack(TANItems.water_bottle);

                if (!player.inventory.addItemStackToInventory(bottleStack))
                {
                    player.dropItem(bottleStack, false);
                }

                // Prevent onItemRightClick from being fired a second time for bottles right clicked on water
                event.setCanceled(true);

            }
            else
            {
                // Restore original amount of bottles
                stack.stackSize = originalCount;
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
        
        if (state.getBlock() instanceof BlockCauldron && player.getHeldItem(event.getHand()).getItem() == Items.GLASS_BOTTLE && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST))
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
