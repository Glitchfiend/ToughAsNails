/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.thirst;

import glitchcore.item.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.TANPotions;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.item.ItemDrink;
import toughasnails.api.stat.capability.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.config.json.DrinkData;
import toughasnails.init.ModConfig;
import toughasnails.thirst.ThirstHandler;

public class DrinkHandler
{
    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            ItemStack stack = player.getHeldItem(player.getActiveHand());
            ThirstHandler thirstHandler = (ThirstHandler)ThirstHelper.getThirstData(player);

            if (thirstHandler.isThirsty())
            {
                // For some reason the stack size can be zero for water bottles, which breaks everything.
                // As a workaround, we temporarily set it to 1
                boolean zeroStack = false;

                if (StackHelper.getSize(stack) <= 0)
                {
                    StackHelper.setSize(stack, 1);
                    zeroStack = true;
                }

                // Special case potions because they use NBT
                if (stack.getItem().equals(Items.POTIONITEM))
                {
                    if ( PotionUtils.getFullEffectsFromItem(stack).isEmpty())
                    {
                        thirstHandler.addStats(7, 0.5F);
                    }
                    else
                    {
                        //Still fill thirst for other potions, but less than water
                        thirstHandler.addStats(4, 0.3F);
                    }
                }
                else if (!(stack.getItem() instanceof ItemDrink))
                {
                    String registryName = stack.getItem().getRegistryName().toString();

                    if (ModConfig.drinkData.containsKey(registryName))
                    {
                        for (DrinkData drinkData : ModConfig.drinkData.get(registryName))
                        {
                            if (drinkData.getPredicate().apply(stack))
                            {
                                applyDrinkStats(player, drinkData);
                                break;
                            }
                        }
                    }
                }

                if (zeroStack) StackHelper.setSize(stack, 0);
            }
        }
    }

    private void applyDrinkStats(EntityPlayer player, DrinkData data)
    {
        IThirst thirst = ThirstHelper.getThirstData(player);
        thirst.addStats(data.getThirstRestored(), data.getHydrationRestored());

        if (player.world.rand.nextFloat() < data.getPoisonChance() && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST))
        {
            player.addPotionEffect(new PotionEffect(TANPotions.thirst, 600));
        }
    }
}
