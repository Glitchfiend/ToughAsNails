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
    private static final ResourceLocation OVERHEATED_HEART_FULL = new ResourceLocation("toughasnails:hud/heart/overheated_full");
    private static final ResourceLocation OVERHEATED_HEART_FULL_BLINKING = new ResourceLocation("toughasnails:hud/heart/overheated_full_blinking");
    private static final ResourceLocation OVERHEATED_HEART_HALF = new ResourceLocation("toughasnails:hud/heart/overheated_half");
    private static final ResourceLocation OVERHEATED_HEART_HALF_BLINKING = new ResourceLocation("toughasnails:hud/heart/overheated_half_blinking");
    private static final ResourceLocation OVERHEATED_HEART_HARDCORE_FULL = new ResourceLocation("toughasnails:hud/heart/overheated_hardcore_full");
    private static final ResourceLocation OVERHEATED_HEART_HARDCORE_FULL_BLINKING = new ResourceLocation("toughasnails:hud/heart/overheated_hardcore_full_blinking");
    private static final ResourceLocation OVERHEATED_HEART_HARDCORE_HALF = new ResourceLocation("toughasnails:hud/heart/overheated_hardcore_half");
    private static final ResourceLocation OVERHEATED_HEART_HARDCORE_HALF_BLINKING = new ResourceLocation("toughasnails:hud/heart/overheated_hardcore_half_blinking");

    public static void heartBlit(GuiGraphics gui, Gui.HeartType heartType, int x, int y, boolean isHardcore, boolean isBlinking, boolean isHalf)
    {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        // Normal hearts
        if (heartType == Gui.HeartType.NORMAL && TemperatureHelper.isFullyHyperthermic(player))
        {
            gui.blitSprite(getOverheatedHeartSprite(isHardcore, isHalf, isBlinking), x, y, 9, 9);
        }
        else
        {
            gui.blitSprite(heartType.getSprite(isHardcore, isHalf, isBlinking), x, y, 9, 9);
        }
    }

    public static void adjustSelectedItemText(GuiGraphics guiGraphics)
    {
        var pose = guiGraphics.pose();

        // If temperature is enabled, move the selected item text up by 2 pixels. This is only done in survival mode.
        if (ModConfig.temperature.enableTemperature && GuiUtils.shouldDrawSurvivalElements())
        {
            pose.translate(0F, -2F, 0F);
        }
    }

    private static ResourceLocation getOverheatedHeartSprite(boolean isHardcore, boolean isHalf, boolean isBlinking)
    {
        if (!isHardcore)
        {
            if (isHalf) {
                return isBlinking ? OVERHEATED_HEART_HALF_BLINKING : OVERHEATED_HEART_HALF;
            } else {
                return isBlinking ? OVERHEATED_HEART_FULL_BLINKING : OVERHEATED_HEART_FULL;
            }
        }
        else if (isHalf)
        {
            return isBlinking ? OVERHEATED_HEART_HARDCORE_HALF_BLINKING : OVERHEATED_HEART_HARDCORE_HALF;
        }
        else
        {
            return isBlinking ? OVERHEATED_HEART_HARDCORE_FULL_BLINKING : OVERHEATED_HEART_HARDCORE_FULL;
        }
    }
}
