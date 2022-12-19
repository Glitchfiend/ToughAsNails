/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.client.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import toughasnails.init.ModTags;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TooltipHandler
{
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        Block block = Block.byItem(stack.getItem());
        BlockState state = block.defaultBlockState();

        if (block == Blocks.AIR)
            return;

        if (state.is(ModTags.Blocks.HEATING_BLOCKS))
        {
            event.getToolTip().add(Component.literal("\uD83D\uDD25 ").append(Component.translatable("desc.toughasnails.heating")).withStyle(ChatFormatting.RED));
        }

        if (state.is(ModTags.Blocks.COOLING_BLOCKS))
        {
            event.getToolTip().add(Component.literal("\u2744 ").append(Component.translatable("desc.toughasnails.cooling")).withStyle(ChatFormatting.AQUA));
        }
    }
}
