/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.item;

import glitchcore.event.EventManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import toughasnails.api.item.TANItems;
import toughasnails.api.thirst.ThirstHelper;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class FilledCanteenItem extends EmptyCanteenItem
{
    private static final int CONSUME_TICKS = 32;

    public FilledCanteenItem(int tier, Properties properties)
    {
        super(tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot)
    {
        if (!(entity instanceof Player player) || stack.getItem() == getPurifiedWaterCanteen() || stack.getEnchantments().isEmpty())
            return;

        // Create a new stack with the same damage value, except purified
        ItemStack newStack = new ItemStack(getPurifiedWaterCanteen());
        newStack.setDamageValue(stack.getDamageValue());
        stack.getEnchantments().entrySet().forEach(e -> newStack.enchant(e.getKey(), e.getIntValue()));

        // Replace the current stack in the player's inventory
        player.getInventory().setItem(slot.getIndex(), newStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int useItemRemainingTicks)
    {
        if (shouldEmitSounds(useItemRemainingTicks))
        {
            entity.playSound(SoundEvents.GENERIC_DRINK.value(), 0.5F, Mth.randomBetween(level.getRandom(), 0.9F, 1.0F));
        }
    }

    private boolean shouldEmitSounds(int useItemRemainingTicks)
    {
        int elapsedTicks = CONSUME_TICKS - useItemRemainingTicks;
        int delay = (int)(CONSUME_TICKS * 0.21875F);
        boolean flag = elapsedTicks > delay;
        return flag && useItemRemainingTicks % 4 == 0;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        InteractionResult result;

        if ((result = this.fillCanteen(level, player, stack)).consumesAction())
        {
            return result;
        }

        if (ThirstHelper.canDrink(player, this.canAlwaysDrink()))
        {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }

        return InteractionResult.FAIL;
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
        if (!worldIn.isClientSide() && !player.getAbilities().instabuild)
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
        return CONSUME_TICKS;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack)
    {
        return ItemUseAnimation.DRINK;
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
