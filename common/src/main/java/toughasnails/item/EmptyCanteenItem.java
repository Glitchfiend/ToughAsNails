/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.enchantment.TANEnchantments;
import toughasnails.api.item.TANItems;
import toughasnails.block.RainCollectorBlock;
import toughasnails.init.ModTags;

public class EmptyCanteenItem extends Item
{
    int tier;

    public EmptyCanteenItem(int tier, Properties properties)
    {
        super(properties);
        this.tier = tier;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand)
    {
        return this.fillCanteen(level, player, player.getItemInHand(hand));
    }

    protected InteractionResultHolder<ItemStack> fillCanteen(Level level, Player player, ItemStack stack)
    {
        HitResult rayTraceResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (rayTraceResult.getType() == HitResult.Type.BLOCK)
        {
            BlockPos pos = ((BlockHitResult)rayTraceResult).getBlockPos();

            if (level.mayInteract(player, pos))
            {
                BlockState state = level.getBlockState(pos);

                if (state.getBlock() instanceof RainCollectorBlock)
                {
                    // Fill the canteen from purified water from a rain collector
                    int waterLevel = state.getValue(RainCollectorBlock.LEVEL);

                    if (waterLevel > 0 && !level.isClientSide())
                    {
                        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        ((RainCollectorBlock) TANBlocks.RAIN_COLLECTOR).setWaterLevel(level, pos, state, waterLevel - 1);
                        return InteractionResultHolder.success(this.replaceCanteen(stack, player, new ItemStack(getPurifiedWaterCanteen())));
                    }
                }
                else if (state.getBlock() == Blocks.WATER_CAULDRON)
                {
                    // Fill the canteen from water from a cauldron
                    int waterLevel = state.getValue(LayeredCauldronBlock.LEVEL);

                    if (waterLevel > 0 && !level.isClientSide())
                    {
                        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                        return InteractionResultHolder.success(this.replaceCanteen(stack, player, new ItemStack(getWaterCanteen())));
                    }
                }
                else if (level.getFluidState(pos).is(FluidTags.WATER))
                {
                    // Fill the canteen with water in the world
                    level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);

                    Holder<Biome> biome = player.level().getBiome(player.blockPosition());
                    Item canteenItem;

                    if (EnchantmentHelper.getItemEnchantmentLevel(TANEnchantments.WATER_CLEANSING, stack) > 0 || biome.is(ModTags.Biomes.PURIFIED_WATER_BIOMES))
                    {
                        canteenItem = getPurifiedWaterCanteen();
                    }
                    else if (biome.is(ModTags.Biomes.DIRTY_WATER_BIOMES))
                    {
                        canteenItem = getDirtyWaterCanteen();
                    }
                    else
                    {
                        canteenItem = getWaterCanteen();
                    }

                    return InteractionResultHolder.sidedSuccess(this.replaceCanteen(stack, player, new ItemStack(canteenItem)), level.isClientSide());
                }
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canBeDepleted()
    {
        return true;
    }

    protected ItemStack replaceCanteen(ItemStack oldStack, Player player, ItemStack newStack)
    {
        player.awardStat(Stats.ITEM_USED.get(this));

        // Copy enchantments from the old stack to the new stack
        EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(oldStack), newStack);
        return ItemUtils.createFilledResult(oldStack, player, newStack);
    }

    public Item getDirtyWaterCanteen()
    {
        switch (this.tier)
        {
            default: case 0: return TANItems.LEATHER_DIRTY_WATER_CANTEEN;
            case 1: return TANItems.COPPER_DIRTY_WATER_CANTEEN;
            case 2: return TANItems.IRON_DIRTY_WATER_CANTEEN;
            case 3: return TANItems.GOLD_DIRTY_WATER_CANTEEN;
            case 4: return TANItems.DIAMOND_DIRTY_WATER_CANTEEN;
            case 5: return TANItems.NETHERITE_DIRTY_WATER_CANTEEN;
        }
    }

    public Item getWaterCanteen()
    {
        switch (this.tier)
        {
            default: case 0: return TANItems.LEATHER_WATER_CANTEEN;
            case 1: return TANItems.COPPER_WATER_CANTEEN;
            case 2: return TANItems.IRON_WATER_CANTEEN;
            case 3: return TANItems.GOLD_WATER_CANTEEN;
            case 4: return TANItems.DIAMOND_WATER_CANTEEN;
            case 5: return TANItems.NETHERITE_WATER_CANTEEN;
        }
    }

    public Item getPurifiedWaterCanteen()
    {
        switch (this.tier)
        {
            default: case 0: return TANItems.LEATHER_PURIFIED_WATER_CANTEEN;
            case 1: return TANItems.COPPER_PURIFIED_WATER_CANTEEN;
            case 2: return TANItems.IRON_PURIFIED_WATER_CANTEEN;
            case 3: return TANItems.GOLD_PURIFIED_WATER_CANTEEN;
            case 4: return TANItems.DIAMOND_PURIFIED_WATER_CANTEEN;
            case 5: return TANItems.NETHERITE_PURIFIED_WATER_CANTEEN;
        }
    }
}
