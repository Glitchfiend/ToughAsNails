/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.thirst.ThirstHelper;

public abstract class DrinkItem extends Item
{
    public DrinkItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving)
    {
        PlayerEntity player = entityLiving instanceof PlayerEntity ? (PlayerEntity)entityLiving : null;

        // Do nothing if this isn't a player
        if (player == null)
            return stack;

        if (player instanceof ServerPlayerEntity)
        {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)player, stack);
        }

        player.awardStat(Stats.ITEM_USED.get(this));

        if (!player.abilities.instabuild)
        {
            stack.shrink(1);

            if (stack.isEmpty())
            {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            player.inventory.add(new ItemStack(Items.GLASS_BOTTLE));
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 32;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.DRINK;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        if (ThirstHelper.canDrink(player, this.canAlwaysDrink()))
        {
            return DrinkHelper.useDrink(world, player, hand);
        }

        return ActionResult.fail(player.getItemInHand(hand));
    }

    public abstract boolean canAlwaysDrink();
}
