/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import toughasnails.api.item.TANItems;

public class EmptyCanteenItem extends Item
{
    public EmptyCanteenItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        RayTraceResult rayTraceResult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);

        if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = ((BlockRayTraceResult)rayTraceResult).getBlockPos();

            if (world.mayInteract(player, pos) && world.getFluidState(pos).is(FluidTags.WATER))
            {
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                return ActionResult.sidedSuccess(this.replaceCanteen(stack, player, new ItemStack(TANItems.water_canteen)), world.isClientSide());
            }
        }

        return ActionResult.pass(stack);
    }

    protected ItemStack replaceCanteen(ItemStack oldStack, PlayerEntity player, ItemStack newStack)
    {
        player.awardStat(Stats.ITEM_USED.get(this));
        return DrinkHelper.createFilledResult(oldStack, player, newStack);
    }
}
