/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import toughasnails.api.TANPotions;
import toughasnails.api.stat.capability.IThirst;
import toughasnails.api.thirst.IDrink;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.thirst.ThirstHandler;

public abstract class ItemDrink<T extends Enum<T> & IDrink> extends Item
{
    public ItemDrink()
    {
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
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
            T type = getTypeFromMeta(stack.getMetadata());
            
            thirst.addStats(type.getThirst(), type.getHydration());
            addEffects(player, type);
            
            return new ItemStack(Items.glass_bottle);
        }

        return stack;
    }
    
    public void addEffects(EntityPlayer player, T type)
    {
        if (player.worldObj.rand.nextFloat() < type.getPoisonChance())
        {
            player.addPotionEffect(new PotionEffect(TANPotions.thirst, 600));
        }
    }
    
    public abstract T getTypeFromMeta(int meta);
    
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
    
    // default behavior in Item is to return 0, but the meta value is important here because it determines which dart type to use
    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }
}
