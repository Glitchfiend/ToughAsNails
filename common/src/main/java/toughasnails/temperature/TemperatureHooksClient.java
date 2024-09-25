/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import glitchcore.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.init.ModConfig;

public class TemperatureHooksClient
{
    private static final ResourceLocation OVERHEATED_HEART_FULL = new ResourceLocation("toughasnails:textures/gui/sprites/hud/heart/overheated_full.png");
    private static final ResourceLocation OVERHEATED_HEART_FULL_BLINKING = new ResourceLocation("toughasnails:textures/gui/sprites/hud/heart/overheated_full_blinking.png");
    private static final ResourceLocation OVERHEATED_HEART_HALF = new ResourceLocation("toughasnails:textures/gui/sprites/hud/heart/overheated_half.png");
    private static final ResourceLocation OVERHEATED_HEART_HALF_BLINKING = new ResourceLocation("toughasnails:textures/gui/sprites/hud/heart/overheated_half_blinking.png");

    public static void heartBlit(GuiGraphics gui,
        int left, int top, int u, int v, int width, int height, boolean isBlinking, boolean isHalf)
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
            gui.blit(getOverheatedHeartSprite(isHalf, isBlinking), left, top, 80 + (iconSubType & 1) * 9, v / 5, width, height);
        }
        else
        {
            gui.blit(Gui.GUI_ICONS_LOCATION, left, top, u, v, width, height);
        }
    }

    public static void adjustSelectedItemText(GuiGraphics guiGraphics)
    {
        var pose = guiGraphics.pose();

        // If temperature is enabled, move the selected item text up by 2 pixels. This is only done in survival mode.
        if (ModConfig.temperature.enableTemperature() && GuiUtils.shouldDrawSurvivalElements())
        {
            pose.translate(0F, -2F, 0F);
        }
    }

    private static ResourceLocation getOverheatedHeartSprite(boolean isHalf, boolean isBlinking)
    {
        if (isHalf) {
            return isBlinking ? OVERHEATED_HEART_HALF_BLINKING : OVERHEATED_HEART_HALF;
        } else {
            return isBlinking ? OVERHEATED_HEART_FULL_BLINKING : OVERHEATED_HEART_FULL;
        }
    }
}
