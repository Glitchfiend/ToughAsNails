/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.client.handler;

import glitchcore.event.client.ItemTooltipEvent;
import glitchcore.event.client.RenderTooltipEvent;
import glitchcore.util.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import toughasnails.init.ModConfig;
import toughasnails.init.ModTags;
import toughasnails.thirst.ThirstOverlayRenderer;

import java.util.Optional;

public class TooltipHandler
{
    public static void onTooltip(ItemTooltipEvent event)
    {
        // Don't display heating or cooling tooltips if temperature is disabled
        if (!ModConfig.temperature.enableTemperature())
            return;

        if (!Environment.isClient())
            throw new IllegalStateException("ItemTooltipEvent unexpectedly called on the server");

        // In some rare cases (on Forge at least) this may be called with a null connection
        if (Minecraft.getInstance().getConnection() == null)
            return;

        ItemStack stack = event.getStack();
        Block block = Block.byItem(stack.getItem());
        BlockState state = block.defaultBlockState();
        RegistryAccess registryAccess = Minecraft.getInstance().getConnection().registryAccess();

        Optional<ArmorTrim> trim = ArmorTrim.getTrim(registryAccess, stack, true);
        Optional<Holder.Reference<TrimMaterial>> trimMaterial = TrimMaterials.getFromIngredient(registryAccess, stack);

        // Heating/Cooling Blocks and Armor/Trimmed Armor
        if (state.is(ModTags.Blocks.HEATING_BLOCKS) || stack.is(ModTags.Items.HEATING_ARMOR) || (trim.isPresent() && trim.get().material().is(ModTags.Trims.HEATING_TRIMS)))
        {
            event.getTooltip().add(Component.literal("\uD83D\uDD25 ").append(Component.translatable("desc.toughasnails.heating")).withStyle(ChatFormatting.GOLD));
        }
        if (state.is(ModTags.Blocks.COOLING_BLOCKS) || stack.is(ModTags.Items.COOLING_ARMOR) || (trim.isPresent() && trim.get().material().is(ModTags.Trims.COOLING_TRIMS)))
        {
            event.getTooltip().add(Component.literal("\u2744 ").append(Component.translatable("desc.toughasnails.cooling")).withStyle(ChatFormatting.AQUA));
        }

        // Heating/Cooling Held Items
        if (stack.is(ModTags.Items.HEATING_HELD_ITEMS))
        {
            event.getTooltip().add(Component.literal("\uD83D\uDD25 ").append(Component.translatable("desc.toughasnails.heating_held")).withStyle(ChatFormatting.GOLD));
        }
        if (stack.is(ModTags.Items.COOLING_HELD_ITEMS))
        {
            event.getTooltip().add(Component.literal("\u2744 ").append(Component.translatable("desc.toughasnails.cooling_held")).withStyle(ChatFormatting.AQUA));
        }

        // Heating/Cooling Consumables
        if (stack.is(ModTags.Items.HEATING_CONSUMED_ITEMS))
        {
            event.getTooltip().add(Component.literal("\uD83D\uDD25 ").append(Component.translatable("desc.toughasnails.heating_consumed")).withStyle(ChatFormatting.GOLD));
        }
        if (stack.is(ModTags.Items.COOLING_CONSUMED_ITEMS))
        {
            event.getTooltip().add(Component.literal("\u2744 ").append(Component.translatable("desc.toughasnails.cooling_consumed")).withStyle(ChatFormatting.AQUA));
        }

        // Heating/Cooling Trim Material Items
        if (trimMaterial.isPresent() && trimMaterial.get().is(ModTags.Trims.HEATING_TRIMS))
        {
            event.getTooltip().add(Component.literal("\uD83D\uDD25 ").append(Component.translatable("desc.toughasnails.heating_trim")).withStyle(ChatFormatting.GOLD));
        }
        if (trimMaterial.isPresent() && trimMaterial.get().is(ModTags.Trims.COOLING_TRIMS))
        {
            event.getTooltip().add(Component.literal("\u2744 ").append(Component.translatable("desc.toughasnails.cooling_trim")).withStyle(ChatFormatting.AQUA));
        }
    }

    public static void onRenderTooltip(RenderTooltipEvent event)
    {
        ItemStack stack = event.getStack();

        // Don't display thirst tooltips if thirst is disabled
        if (!ModConfig.thirst.enableThirst())
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
