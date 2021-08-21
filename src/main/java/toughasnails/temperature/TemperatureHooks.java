/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.core.ToughAsNails;

public class TemperatureHooks
{
    @OnlyIn(Dist.CLIENT)
    public static void heartBlit(Gui gui, PoseStack stack, int left, int top, int u, int v, int width, int height)
    {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        int iconBase = (u - 16) / 9;
        int iconType = iconBase / 4;
        int iconSubType = iconBase % 4;

        gui.blit(stack, left, top, u, v, width, height);

        // Normal hearts
        if (iconType == 1 && TemperatureHelper.isFullyHyperthermic(player))
        {
            RenderSystem.setShaderTexture(0, TemperatureOverlayHandler.OVERLAY);
            gui.blit(stack, left, top, 80 + (iconSubType & 1) * 9, v / 5, width, height);
            RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
        }
        else
        {
            gui.blit(stack, left, top, u, v, width, height);
        }
    }
}
