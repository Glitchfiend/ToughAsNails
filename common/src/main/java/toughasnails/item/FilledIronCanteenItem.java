/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import toughasnails.api.item.TANItems;

public class FilledIronCanteenItem extends Item
{
    public FilledIronCanteenItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        HitResult rayTraceResult = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);

        if (rayTraceResult.getType() == HitResult.Type.BLOCK)
        {
            BlockPos pos = ((BlockHitResult)rayTraceResult).getBlockPos();

            if (world.mayInteract(player, pos) && world.getFluidState(pos).is(FluidTags.WATER))
            {
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                return InteractionResultHolder.sidedSuccess(this.replaceCanteen(stack, player, new ItemStack(TANItems.IRON_WATER_CANTEEN)), world.isClientSide());
            }
        }

        return ItemUtils.startUsingInstantly(world, player, hand);
    }

    protected ItemStack replaceCanteen(ItemStack oldStack, Player player, ItemStack newStack)
    {
        player.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.createFilledResult(oldStack, player, newStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving)
    {
        Player player = entityLiving instanceof Player ? (Player)entityLiving : null;

        // Do nothing if this isn't a player
        if (player == null)
            return stack;

        player.awardStat(Stats.ITEM_USED.get(this));

        // Damage the item if we're on the server and the player isn't in creative mode
        if (!worldIn.isClientSide && !player.getAbilities().instabuild)
        {
            boolean[] broken = new boolean[]{false};
            stack.hurtAndBreak(1, player, (entity) -> broken[0] = true);
            if (broken[0])
            {
                return new ItemStack(TANItems.EMPTY_IRON_CANTEEN);
            }
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.DRINK;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }
}
