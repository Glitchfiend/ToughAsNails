/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.client.handler;

import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import toughasnails.init.ModTags;
import toughasnails.thirst.ThirstOverlayRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TooltipHandler
{
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        Block block = Block.byItem(stack.getItem());
        BlockState state = block.defaultBlockState();

        if (stack.is(ModTags.Items.HEATING_ITEMS) || stack.is(ModTags.Items.HEATING_ARMOR) || state.is(ModTags.Blocks.HEATING_BLOCKS))
        {
            event.getToolTip().add(Component.literal("\uD83D\uDD25 ").append(Component.translatable("desc.toughasnails.heating")).withStyle(ChatFormatting.GOLD));
        }

        if (stack.is(ModTags.Items.COOLING_ITEMS) || stack.is(ModTags.Items.COOLING_ARMOR) || state.is(ModTags.Blocks.COOLING_BLOCKS))
        {
            event.getToolTip().add(Component.literal("\u2744 ").append(Component.translatable("desc.toughasnails.cooling")).withStyle(ChatFormatting.AQUA));
        }
    }

    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.GatherComponents event)
    {
        ItemStack stack = event.getItemStack();

        if (stack.is(ModTags.Items.DRINKS))
        {
            event.getTooltipElements().add(Either.right(new ThirstTooltipComponent(ModTags.Items.getThirstRestored(stack))));
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    private static class ClientTooltipRegisterHandler
    {
        @SubscribeEvent
        public static void onRegisterFactories(RegisterClientTooltipComponentFactoriesEvent event)
        {
            event.register(ThirstTooltipComponent.class, component -> new ThirstClientTooltipComponent(component.getAmount()));
        }
    }

    private static class ThirstTooltipComponent implements TooltipComponent
    {
        private final int amount;

        private ThirstTooltipComponent(int amount)
        {
            this.amount = amount;
        }

        public int getAmount()
        {
            return this.amount;
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
            return (this.amount / 2) * 9;
        }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics gui)
        {
            gui.pose().pushPose();

            for (int i = 0; i < Mth.ceil(this.amount / 2.0F); i++)
            {
                int dropletHalf = i * 2 + 1;

                int startX = x + i * 8 - 1;
                int startY = y;

                // Args: poseStack, x, y, u, v, width, height, texWidth, texHeight
                // Draw a full droplet
                if (this.amount > dropletHalf)
                {
                    gui.blit(ThirstOverlayRenderer.OVERLAY, startX, startY, 0, 32 + 9, 9, 9, 256, 256);
                }
                else if (this.amount == dropletHalf) // Draw a half droplet
                {
                    gui.blit(ThirstOverlayRenderer.OVERLAY, startX, startY, 9, 32 + 9, 9, 9, 256, 256);
                }
            }

            gui.pose().popPose();
        }
    }
}
