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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.block.RainCollectorBlock;
import toughasnails.init.ModTags;

public class EmptyGoldCanteenItem extends Item
{
    public EmptyGoldCanteenItem(Properties properties)
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

            if (world.mayInteract(player, pos))
            {
                BlockState state = world.getBlockState(pos);

                if (state.getBlock() instanceof RainCollectorBlock)
                {
                    // Fill the canteen from purified water from a rain collector
                    int waterLevel = state.getValue(RainCollectorBlock.LEVEL);

                    if (waterLevel > 0 && !world.isClientSide())
                    {
                        world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        ((RainCollectorBlock) TANBlocks.RAIN_COLLECTOR).setWaterLevel(world, pos, state, waterLevel - 1);
                        return InteractionResultHolder.success(this.replaceCanteen(stack, player, new ItemStack(TANItems.GOLD_PURIFIED_WATER_CANTEEN)));
                    }
                }
                else if (world.getFluidState(pos).is(FluidTags.WATER))
                {
                    // Fill the canteen with water in the world
                    world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);

                    Holder<Biome> biome = player.level().getBiome(player.blockPosition());
                    Item canteenItem;

                    if (biome.is(ModTags.Biomes.DIRTY_WATER_BIOMES))
                    {
                        canteenItem = TANItems.GOLD_DIRTY_WATER_CANTEEN;
                    }
                    else if (biome.is(ModTags.Biomes.PURIFIED_WATER_BIOMES))
                    {
                        canteenItem = TANItems.GOLD_PURIFIED_WATER_CANTEEN;
                    }
                    else
                    {
                        canteenItem = TANItems.GOLD_WATER_CANTEEN;
                    }

                    return InteractionResultHolder.sidedSuccess(this.replaceCanteen(stack, player, new ItemStack(canteenItem)), world.isClientSide());
                }
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    protected ItemStack replaceCanteen(ItemStack oldStack, Player player, ItemStack newStack)
    {
        player.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.createFilledResult(oldStack, player, newStack);
    }
}
