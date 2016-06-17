/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import toughasnails.api.HealthHelper;

public class ItemLifebloodCrystal extends Item
{
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
    	if (HealthHelper.addActiveHearts(player, 1))
    	{
    		for (int i = 0; i < 8; i++)
    		{
    			double d0 = world.rand.nextGaussian() * 0.02D;
            	double d1 = world.rand.nextGaussian() * 0.02D;
            	double d2 = world.rand.nextGaussian() * 0.02D;
            	world.spawnParticle(EnumParticleTypes.HEART, player.posX + (double)(world.rand.nextFloat() * player.width * 2.0F) - (double)player.width, player.posY + 0.5D + (double)(world.rand.nextFloat() * player.height), player.posZ + (double)(world.rand.nextFloat() * player.width * 2.0F) - (double)player.width, d0, d1, d2, new int[0]);
    		}
    		world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.75F, 1.0F);
            stack.stackSize--;
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    	}
    	else
    	{
    		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    	}
    }
    
    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}
