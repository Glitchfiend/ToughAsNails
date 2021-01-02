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
import net.minecraft.inventory.EquipmentSlotType;
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
import toughasnails.api.thirst.WaterType;

public class CanteenItem extends Item
{
    private WaterType waterType;

    public CanteenItem(WaterType waterType, Properties properties)
    {
        super(properties);
        this.waterType = waterType;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        RayTraceResult rayTraceResult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);

        if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = ((BlockRayTraceResult)rayTraceResult).getBlockPos();

            if (world.mayInteract(player, pos) && world.getFluidState(pos).is(FluidTags.WATER) && (this.waterType == WaterType.NORMAL || this.waterType == WaterType.UNKNOWN))
            {
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                return ActionResult.sidedSuccess(this.replaceCanteen(stack, player, new ItemStack(TANItems.normal_water_canteen)), world.isClientSide());
            }
        }

        if (this.waterType != WaterType.UNKNOWN)
        {
            return DrinkHelper.useDrink(world, player, hand);
        }

        return ActionResult.pass(stack);
    }

    protected ItemStack replaceCanteen(ItemStack oldStack, PlayerEntity player, ItemStack newStack)
    {
        player.awardStat(Stats.ITEM_USED.get(this));
        return DrinkHelper.createFilledResult(oldStack, player, newStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
        PlayerEntity player = entityLiving instanceof PlayerEntity ? (PlayerEntity)entityLiving : null;

        // Do nothing if this isn't a player
        if (player == null)
            return stack;

        player.awardStat(Stats.ITEM_USED.get(this));

        // Damage the item if we're on the server and the player isn't in creative mode
        if (!worldIn.isClientSide && !player.abilities.instabuild)
        {
            boolean[] broken = new boolean[]{false};
            stack.hurtAndBreak(1, player, (entity) -> broken[0] = true);
            if (broken[0])
            {
                return new ItemStack(TANItems.empty_canteen);
            }
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        switch (this.waterType)
        {
            case UNKNOWN:
                return 0;

            default:
                return 32;
        }
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        switch (this.waterType)
        {
            case UNKNOWN:
                return UseAction.NONE;

            default:
                return UseAction.DRINK;
        }
    }
}
