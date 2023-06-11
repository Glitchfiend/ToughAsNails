/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import toughasnails.api.temperature.TemperatureHelper;

public class TemperatureHooks
{
    @OnlyIn(Dist.CLIENT)
    public static void heartBlit(GuiGraphics gui, int left, int top, int u, int v, int width, int height)
    {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        int iconBase = (u - 16) / 9;
        int iconType = iconBase / 4;
        int iconSubType = iconBase % 4;

        gui.blit(Gui.GUI_ICONS_LOCATION, left, top, u, v, width, height);

        // Normal hearts
        if (iconType == 1 && TemperatureHelper.isFullyHyperthermic(player))
        {
            gui.blit(TemperatureOverlayHandler.OVERLAY, left, top, 80 + (iconSubType & 1) * 9, v / 5, width, height);
        }
        else
        {
            gui.blit(Gui.GUI_ICONS_LOCATION, left, top, u, v, width, height);
        }
    }
}
