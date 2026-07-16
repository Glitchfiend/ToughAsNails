/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import glitchcore.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2f;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.init.ModConfig;

public class TemperatureHooksClient
{
    private static final Identifier OVERHEATED_HEART_FULL = Identifier.parse("toughasnails:hud/heart/overheated_full");
    private static final Identifier OVERHEATED_HEART_FULL_BLINKING = Identifier.parse("toughasnails:hud/heart/overheated_full_blinking");
    private static final Identifier OVERHEATED_HEART_HALF = Identifier.parse("toughasnails:hud/heart/overheated_half");
    private static final Identifier OVERHEATED_HEART_HALF_BLINKING = Identifier.parse("toughasnails:hud/heart/overheated_half_blinking");
    private static final Identifier OVERHEATED_HEART_HARDCORE_FULL = Identifier.parse("toughasnails:hud/heart/overheated_hardcore_full");
    private static final Identifier OVERHEATED_HEART_HARDCORE_FULL_BLINKING = Identifier.parse("toughasnails:hud/heart/overheated_hardcore_full_blinking");
    private static final Identifier OVERHEATED_HEART_HARDCORE_HALF = Identifier.parse("toughasnails:hud/heart/overheated_hardcore_half");
    private static final Identifier OVERHEATED_HEART_HARDCORE_HALF_BLINKING = Identifier.parse("toughasnails:hud/heart/overheated_hardcore_half_blinking");

    public static void heartBlit(GuiGraphicsExtractor gui, Hud.HeartType heartType, int x, int y, boolean isHardcore, boolean isBlinking, boolean isHalf)
    {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        // Normal hearts
        if (heartType == Hud.HeartType.NORMAL && TemperatureHelper.isFullyHyperthermic(player))
        {
            gui.blitSprite(RenderPipelines.GUI_TEXTURED, getOverheatedHeartSprite(isHardcore, isHalf, isBlinking), x, y, 9, 9);
        }
        else
        {
            gui.blitSprite(RenderPipelines.GUI_TEXTURED, heartType.getSprite(isHardcore, isHalf, isBlinking), x, y, 9, 9);
        }
    }

    public static void adjustSelectedItemText(GuiGraphicsExtractor GuiGraphicsExtractor)
    {
        var pose = GuiGraphicsExtractor.pose();

        // If temperature is enabled, move the selected item text up by 2 pixels. This is only done in survival mode.
        if (ModConfig.temperature.enableTemperature && GuiUtils.shouldDrawSurvivalElements())
        {
            pose.translate(0F, -2F, new Matrix3x2f());
        }
    }

    private static Identifier getOverheatedHeartSprite(boolean isHardcore, boolean isHalf, boolean isBlinking)
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
