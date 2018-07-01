/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import toughasnails.api.TANPotions;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.stat.capability.IThirst;
import toughasnails.api.thirst.IDrink;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.api.thirst.WaterType;
import toughasnails.thirst.ThirstHandler;

public class ItemPurifiedWaterBottle extends Item implements IDrink
{
    public ItemPurifiedWaterBottle()
    {
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
	
    public int getThirst()
    {
        return WaterType.PURIFIED.getThirst();
    }

    public float getHydration()
    {
        return WaterType.PURIFIED.getThirst();
    }

    public float getPoisonChance()
    {
        return WaterType.PURIFIED.getThirst();
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        ThirstHandler thirstHandler = (ThirstHandler)ThirstHelper.getThirstData(player);
        
        if (thirstHandler.isThirsty())
        {
            player.setActiveHand(hand);
        }
        
        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        if (!world.isRemote && entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;
            IThirst thirst = ThirstHelper.getThirstData(player);
            
            if (!player.capabilities.isCreativeMode)
            {
                stack.shrink(1);
            }
            
            thirst.addStats(this.getThirst(), this.getHydration());
            
            if (!player.capabilities.isCreativeMode)
            {
                if (stack.isEmpty())
                {
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
                
                player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return stack;
    }
}
