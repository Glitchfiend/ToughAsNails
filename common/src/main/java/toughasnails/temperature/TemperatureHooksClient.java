/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import glitchcore.util.GuiUtils;
import net.minecraft.client.gui.GuiGraphics;
import toughasnails.init.ModConfig;

public class TemperatureHooksClient
{
    public static void heartBlit(GuiGraphics gui, int left, int top, int u, int v, int width, int height) {
        int iconBase = (u - 16) / 9;
        int iconSubType = iconBase % 4;

        gui.blit(TemperatureOverlayRenderer.OVERLAY, left, top, 80 + (iconSubType & 1) * 9, v / 5, width, height);
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
}
