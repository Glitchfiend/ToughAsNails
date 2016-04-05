/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.api.thirst.WaterType;
import toughasnails.thirst.ThirstHandler;

public class VanillaDrinkHandler 
{
    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            ItemStack stack = event.getItem();
            ThirstHandler thirstHandler = (ThirstHandler)ThirstHelper.getThirstData(player);
            
            if (thirstHandler.isThirsty())
            {
                if (stack.getItem() == Items.milk_bucket)
                {
                    thirstHandler.addStats(8, 0.5F);
                }
                else if (stack.getItem() == Items.potionitem)
                {
                    if ( PotionUtils.getFullEffectsFromItem(stack).isEmpty())
                    {
                        thirstHandler.addStats(4, 0.6F);
                    }
                    else
                    {
                        //Still fill thirst for other potions, but less than water
                        thirstHandler.addStats(2, 0.3F);
                    }
                }
            }
        }
    }
}
