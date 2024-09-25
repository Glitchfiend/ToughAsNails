/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

import glitchcore.event.TickEvent;
import glitchcore.event.client.RenderGuiEvent;
import glitchcore.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.init.ModConfig;

import java.util.Random;

public class ThirstOverlayRenderer
{
    private static final Random RANDOM = new Random();
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/icons.png");

    private static int updateCounter;

    public static void onClientTick(TickEvent.Client event)
    {
        Minecraft minecraft = Minecraft.getInstance();

        if (event.getPhase() == TickEvent.Phase.END && !minecraft.isPaused())
        {
            updateCounter++;
        }
    }

    public static void onBeginRenderAir(RenderGuiEvent.Pre event)
    {
        if (event.getType() != RenderGuiEvent.Type.AIR || !ModConfig.thirst.enableThirst())
            return;

        Minecraft minecraft = Minecraft.getInstance();
        Entity vehicle = minecraft.player.getVehicle();
        boolean isMounted = vehicle != null && vehicle.showVehicleHealth();
        if (!isMounted && !minecraft.options.hideGui && GuiUtils.shouldDrawSurvivalElements())
        {
            GuiUtils.setupOverlayRenderState(true, false);
            Player player = minecraft.player;
            IThirst thirst = ThirstHelper.getThirst(player);

            // When the update counter isn't incrementing, ensure the same numbers are produced (freezes moving gui elements)
            RANDOM.setSeed(updateCounter * 312871L);

            int rowTop = event.getRowTop();
            drawThirst(event.getGuiGraphics(), event.getScreenWidth(), rowTop, thirst.getThirst(), thirst.getHydration());
            event.setRowTop(rowTop - 10);
        }
    }

    public static void drawThirst(GuiGraphics guiGraphics, int screenWidth, int rowTop, int thirstLevel, float thirstHydrationLevel)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        int left = screenWidth / 2 + 91 + ModConfig.client.thirstLeftOffset();
        int top = rowTop + ModConfig.client.thirstTopOffset();

        for (int i = 0; i < 10; i++)
        {
            int dropletHalf = i * 2 + 1;
            int iconIndex = 0;

            int startX = left - i * 8 - 9;
            int startY = top;

            int backgroundU = 0;

            if (player.hasEffect(TANEffects.THIRST))
            {
                iconIndex += 4;
                backgroundU += 117;
            }

            if (thirstHydrationLevel <= 0.0F && updateCounter % (thirstLevel * 3 + 1) == 0)
            {
                startY = top + (RANDOM.nextInt(3) - 1);
            }

            // Draw the background of each thirst droplet
            guiGraphics.blit(OVERLAY, startX, startY, backgroundU, 32, 9, 9);

            // Draw a full droplet
            if (thirstLevel > dropletHalf)
            {
                guiGraphics.blit(OVERLAY, startX, startY, (iconIndex + 4) * 9, 32, 9, 9);
            }
            else if (thirstLevel == dropletHalf) // Draw a half droplet
            {
                guiGraphics.blit(OVERLAY, startX, startY, (iconIndex + 5) * 9, 32, 9, 9);
            }
        }
    }
}
