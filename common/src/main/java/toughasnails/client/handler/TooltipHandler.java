/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.client.handler;

import glitchcore.event.client.ItemTooltipEvent;
import glitchcore.event.client.RenderTooltipEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModConfig;
import toughasnails.init.ModTags;
import toughasnails.thirst.ThirstOverlayRenderer;

public class TooltipHandler
{
    public static void onTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getStack();
        Block block = Block.byItem(stack.getItem());
        BlockState state = block.defaultBlockState();

        // Don't display heating or cooling tooltips if temperature is disabled
        if (!ModConfig.temperature.enableTemperature)
            return;

        if (state.is(ModTags.Blocks.HEATING_BLOCKS) || stack.is(ModTags.Items.HEATING_ARMOR))
        {
            event.getTooltip().add(Component.literal("\uD83D\uDD25 ").append(Component.translatable("desc.toughasnails.heating")).withStyle(ChatFormatting.GOLD));
        }

        if (state.is(ModTags.Blocks.COOLING_BLOCKS) || stack.is(ModTags.Items.COOLING_ARMOR))
        {
            event.getTooltip().add(Component.literal("\u2744 ").append(Component.translatable("desc.toughasnails.cooling")).withStyle(ChatFormatting.AQUA));
        }

        if (stack.is(ModTags.Items.HEATING_HELD_ITEMS))
        {
            event.getTooltip().add(Component.literal("\uD83D\uDD25 ").append(Component.translatable("desc.toughasnails.heating_held")).withStyle(ChatFormatting.GOLD));
        }

        if (stack.is(ModTags.Items.COOLING_HELD_ITEMS))
        {
            event.getTooltip().add(Component.literal("\u2744 ").append(Component.translatable("desc.toughasnails.cooling_held")).withStyle(ChatFormatting.AQUA));
        }
    }

    public static void onRenderTooltip(RenderTooltipEvent event)
    {
        ItemStack stack = event.getStack();

        // Don't display thirst tooltips if thirst is disabled
        if (!ModConfig.thirst.enableThirst)
            return;

        if (stack.is(ModTags.Items.DRINKS))
        {
            event.getComponents().add(new ThirstClientTooltipComponent(ModTags.Items.getThirstRestored(stack)));
        }
    }

    private static class ThirstClientTooltipComponent implements ClientTooltipComponent
    {
        private final int amount;

        private ThirstClientTooltipComponent(int amount)
        {
            this.amount = amount;
        }

        @Override
        public int getHeight()
        {
            return 9;
        }

        @Override
        public int getWidth(Font font)
        {
            return (this.amount / 2) * 8;
        }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics gui)
        {
            gui.pose().pushPose();

            for (int i = 0; i < Mth.ceil(this.amount / 2.0F); i++)
            {
                int dropletHalf = i * 2 + 1;

                int startX = x + i * 8;
                int startY = y;

                // Args: poseStack, x, y, u, v, width, height, texWidth, texHeight
                // Draw a full droplet
                if (this.amount > dropletHalf)
                {
                    gui.blit(ThirstOverlayRenderer.OVERLAY, startX, startY, 0, 41, 8, 8, 256, 256);
                }
                else if (this.amount == dropletHalf) // Draw a half droplet
                {
                    gui.blit(ThirstOverlayRenderer.OVERLAY, startX, startY, 8, 41, 8, 8, 256, 256);
                }
            }

            gui.pose().popPose();
        }
    }
}
