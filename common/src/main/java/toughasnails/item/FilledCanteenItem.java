/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import toughasnails.api.item.TANItems;
import toughasnails.api.thirst.ThirstHelper;

import java.util.concurrent.atomic.AtomicBoolean;

public class FilledCanteenItem extends EmptyCanteenItem
{
    public FilledCanteenItem(int tier, Properties properties)
    {
        super(tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected)
    {
        if (!(entity instanceof Player player) || stack.getItem() == getPurifiedWaterCanteen() || stack.getEnchantments().isEmpty())
            return;

        // Create a new stack with the same damage value, except purified
        ItemStack newStack = new ItemStack(getPurifiedWaterCanteen());
        newStack.setDamageValue(stack.getDamageValue());
        stack.getEnchantments().entrySet().forEach(e -> newStack.enchant(e.getKey(), e.getIntValue()));

        // Replace the current stack in the player's inventory
        player.getInventory().setItem(slot, newStack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> result;

        if ((result = this.fillCanteen(level, player, stack)).getResult().consumesAction())
        {
            return result;
        }

        if (ThirstHelper.canDrink(player, this.canAlwaysDrink()))
        {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }

        return InteractionResultHolder.fail(stack);
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
            ItemStack emptyStack = new ItemStack(getEmptyCanteen());
            stack.getEnchantments().entrySet().forEach(e -> emptyStack.enchant(e.getKey(), e.getIntValue()));

            AtomicBoolean broken = new AtomicBoolean(false);
            stack.hurtAndBreak(1, (ServerLevel)worldIn, (ServerPlayer)player, item -> broken.set(true));
            if (broken.get())
            {
                return emptyStack;
            }
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity)
    {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.DRINK;
    }

    public boolean canAlwaysDrink()
    {
        return false;
    }

    public Item getEmptyCanteen()
    {
        switch (this.tier)
        {
            default: case 0: return TANItems.EMPTY_LEATHER_CANTEEN;
            case 1: return TANItems.EMPTY_COPPER_CANTEEN;
            case 2: return TANItems.EMPTY_IRON_CANTEEN;
            case 3: return TANItems.EMPTY_GOLD_CANTEEN;
            case 4: return TANItems.EMPTY_DIAMOND_CANTEEN;
            case 5: return TANItems.EMPTY_NETHERITE_CANTEEN;
        }
    }
}
