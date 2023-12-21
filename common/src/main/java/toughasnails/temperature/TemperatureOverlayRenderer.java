/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import glitchcore.event.RenderGuiEvent;
import glitchcore.event.TickEvent;
import glitchcore.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.TANAPI;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.init.ModConfig;

import java.util.Random;

public class TemperatureOverlayRenderer
{
    private static final Random RANDOM = new Random();
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/icons.png");
    private static final ResourceLocation HYPERTHERMIA_OUTLINE_LOCATION = new ResourceLocation(TANAPI.MOD_ID, "textures/misc/hyperthermia_outline.png");
    private static long updateCounter;
    private static long flashCounter;
    private static TemperatureLevel prevTemperatureLevel;

    public static void onBeginRenderFood(RenderGuiEvent.Pre event)
    {
        if (event.getType() != RenderGuiEvent.Type.FOOD)
            return;

        Gui gui = event.getGui();
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.options.hideGui && GuiUtils.shouldDrawSurvivalElements())
        {
            GuiUtils.setupOverlayRenderState(true, false);
            renderTemperature(event.getGuiGraphics(), event.getPartialTicks(), event.getScreenWidth(), event.getScreenHeight());
        }
    }

    public static void onBeginRenderFrostbite(RenderGuiEvent.Pre event)
    {
        if (event.getType() != RenderGuiEvent.Type.FROSTBITE)
            return;

        GuiUtils.setupOverlayRenderState(true, false);

        Gui gui = event.getGui();
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (TemperatureHelper.getTicksHyperthermic(player) > 0)
            gui.renderTextureOverlay(event.getGuiGraphics(), HYPERTHERMIA_OUTLINE_LOCATION, TemperatureHelper.getPercentHyperthermic(player));
    }

    public static void onClientTick(TickEvent.Client event)
    {
        Minecraft minecraft = Minecraft.getInstance();

        if (event.getPhase() == TickEvent.Phase.END && !minecraft.isPaused())
        {
            updateCounter++;
        }
    }

    private static void renderTemperature(GuiGraphics guiGraphics, float partialTicks, int width, int height)
    {
        Minecraft minecraft = Minecraft.getInstance();

        // Do nothing if temperature is disabled
        if (!ModConfig.temperature.enableTemperature)
            return;

        Player player = minecraft.player;
        TemperatureLevel temperature = TemperatureHelper.getTemperatureForPlayer(player);

        // When the update counter isn't incrementing, ensure the same numbers are produced (freezes moving gui elements)
        RANDOM.setSeed(updateCounter * 312871L);

        if (minecraft.gameMode.getPlayerMode().isSurvival())
        {
            drawTemperature(guiGraphics, width, height, temperature);
        }
    }

    private static void drawTemperature(GuiGraphics gui, int width, int height, TemperatureLevel temperature)
    {
        int left = width / 2 - 8 + ModConfig.client.temperatureLeftOffset;
        int top = height - 52 + ModConfig.client.temperatureTopOffset;

        if (prevTemperatureLevel == null)
            prevTemperatureLevel = temperature;

        // Flash for 16 ticks when the temperature changes
        if (prevTemperatureLevel != temperature)
        {
            flashCounter = updateCounter + 3;
        }

        // Update the prevTemperatureLevel to the current temperature level
        prevTemperatureLevel = temperature;

        // Shake the temperature meter when ICY or HOT
        if (temperature == TemperatureLevel.ICY || temperature == TemperatureLevel.HOT)
        {
            if ((updateCounter % 1) == 0)
            {
                top += (int)((RANDOM.nextInt(3) - 1) * Math.min(3F, 1.0));
                left += (int)((RANDOM.nextInt(3) - 1) * Math.min(1.5F, 1.0));
            }
        }

        int iconIndex = temperature.ordinal() * 16;
        int v = 0;

        // Adjust v for flashing
        if (flashCounter > updateCounter)
            v += 16;

        gui.blit(OVERLAY, left, top, iconIndex, v, 16, 16);
    }
}
