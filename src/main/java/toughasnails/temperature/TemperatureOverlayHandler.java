/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.gui.GuiUtils;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.config.ServerConfig;
import toughasnails.core.ToughAsNails;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class TemperatureOverlayHandler
{
    private static final Random RANDOM = new Random();
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/icons.png");
    private static final ResourceLocation HYPERTHERMIA_OUTLINE_LOCATION = new ResourceLocation(ToughAsNails.MOD_ID, "textures/misc/hyperthermia_outline.png");

    public static final IIngameOverlay TEMPERATURE_LEVEL_ELEMENT = OverlayRegistry.registerOverlayAbove(ForgeIngameGui.FOOD_LEVEL_ELEMENT, "Temperature Level", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements())
        {
            gui.setupOverlayRenderState(true, false);
            renderTemperature(gui, mStack, partialTicks, screenWidth, screenHeight);
        }
    });

    public static final IIngameOverlay HYPERTHERMIA_ELEMENT = OverlayRegistry.registerOverlayAbove(ForgeIngameGui.FROSTBITE_ELEMENT, "Hyperthermia", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
        gui.setupOverlayRenderState(true, false);
        renderHyperthermia(gui, mStack);
    });

    private static long updateCounter;
    private static long flashCounter;
    private static TemperatureLevel prevTemperatureLevel;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft minecraft = Minecraft.getInstance();

        if (ServerConfig.enableTemperature.get() && event.phase == TickEvent.Phase.END && !minecraft.isPaused())
        {
            updateCounter++;
        }
    }

    private static void renderTemperature(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height)
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
            RenderSystem.setShaderTexture(0, OVERLAY);
            drawTemperature(mStack, width, height, temperature);
            RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
        }
    }

    private static void renderHyperthermia(ForgeIngameGui gui, PoseStack pStack)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (TemperatureHelper.getTicksHyperthermic(player) > 0)
            gui.renderTextureOverlay(HYPERTHERMIA_OUTLINE_LOCATION, TemperatureHelper.getPercentHyperthermic(player));
    }

    private static void drawTemperature(PoseStack matrixStack, int width, int height, TemperatureLevel temperature)
    {
        int left = width / 2 - 8;
        int top = height - 52;

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

        GuiUtils.drawTexturedModalRect(matrixStack, left, top, iconIndex, v, 16, 16, 9);
    }
}
