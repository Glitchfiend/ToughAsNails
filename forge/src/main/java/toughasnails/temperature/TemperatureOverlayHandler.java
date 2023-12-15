/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.config.ClientConfig;
import toughasnails.config.ServerConfig;
import toughasnails.core.ToughAsNailsForge;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TemperatureOverlayHandler
{
    private static final Random RANDOM = new Random();
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/icons.png");
    private static final ResourceLocation HYPERTHERMIA_OUTLINE_LOCATION = new ResourceLocation(ToughAsNailsForge.MOD_ID, "textures/misc/hyperthermia_outline.png");

    private static long updateCounter;
    private static long flashCounter;
    private static TemperatureLevel prevTemperatureLevel;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft minecraft = Minecraft.getInstance();

        if (event.phase == TickEvent.Phase.END && !minecraft.isPaused())
        {
            updateCounter++;
        }
    }

    private static void renderTemperature(ForgeGui gui, GuiGraphics guiGraphics, float partialTicks, int width, int height)
    {
        Minecraft minecraft = Minecraft.getInstance();

        // Do nothing if temperature is disabled
        if (!ServerConfig.enableTemperature.get())
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

    private static void renderHyperthermia(ForgeGui gui, GuiGraphics guiGraphics)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (TemperatureHelper.getTicksHyperthermic(player) > 0)
            gui.renderTextureOverlay(guiGraphics, HYPERTHERMIA_OUTLINE_LOCATION, TemperatureHelper.getPercentHyperthermic(player));
    }

    private static void drawTemperature(GuiGraphics gui, int width, int height, TemperatureLevel temperature)
    {
        int left = width / 2 - 8 + ClientConfig.temperatureLeftOffset.get();
        int top = height - 52 + ClientConfig.temperatureTopOffset.get();

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

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    private static class OverlayRegister
    {
        @SubscribeEvent
        public static void registerOverlays(RegisterGuiOverlaysEvent event)
        {
            event.registerAbove(VanillaGuiOverlay.FOOD_LEVEL.id(), "temperature_level", (gui, poseStack, partialTick, screenWidth, screenHeight) ->
            {
                Minecraft minecraft = Minecraft.getInstance();
                if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements())
                {
                    gui.setupOverlayRenderState(true, false);
                    renderTemperature(gui, poseStack, partialTick, screenWidth, screenHeight);
                }
            });

            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "hyperthermia", (gui, poseStack, partialTick, screenWidth, screenHeight) ->
            {
                gui.setupOverlayRenderState(true, false);
                renderHyperthermia(gui, poseStack);
            });
        }
    }
}
