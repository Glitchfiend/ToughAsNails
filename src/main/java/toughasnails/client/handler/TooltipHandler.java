/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.gui.ScreenUtils;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import toughasnails.api.potion.TANEffects;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModTags;
import toughasnails.thirst.ThirstOverlayHandler;

import java.util.List;

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

    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.GatherComponents event)
    {
        ItemStack stack = event.getItemStack();

        if (stack.is(ModTags.Items.DRINKS))
        {
            event.getTooltipElements().add(Either.right(new ThirstTooltipComponent(ModTags.Items.getThirstRestored(stack))));
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
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
        public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset)
        {
            poseStack.pushPose();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, ThirstOverlayHandler.OVERLAY);

            for (int i = 0; i < Mth.ceil(this.amount / 2.0F); i++)
            {
                int dropletHalf = i * 2 + 1;

                int startX = x + i * 8;
                int startY = y;

                // Draw the background of each thirst droplet
                // Args: poseStack, x, y, u, v, width, height, texWidth, texHeight
                GuiComponent.blit(poseStack, startX, startY, 9, 32, 9, 9, 256, 256);

                // Draw a full droplet
                if (this.amount > dropletHalf)
                {
                    GuiComponent.blit(poseStack, startX, startY, 4 * 9, 32, 9, 9, 256, 256);
                }
                else if (this.amount == dropletHalf) // Draw a half droplet
                {
                    GuiComponent.blit(poseStack, startX, startY, 9, 32 + 9, 9, 9, 256, 256);
                }
            }

            poseStack.popPose();
        }
    }
}
