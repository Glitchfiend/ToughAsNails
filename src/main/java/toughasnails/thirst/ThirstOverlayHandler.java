/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

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
import toughasnails.api.potion.TANEffects;
import toughasnails.api.thirst.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.config.ClientConfig;
import toughasnails.config.ServerConfig;

import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ThirstOverlayHandler
{
    private static final Random RANDOM = new Random();
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/icons.png");

    private static int updateCounter;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft minecraft = Minecraft.getInstance();

        if (event.phase == TickEvent.Phase.END && !minecraft.isPaused())
        {
            updateCounter++;
        }
    }

    private static void renderThirst(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTicks, int width, int height)
    {
        Minecraft minecraft = Minecraft.getInstance();

        // Do nothing if thirst is disabled
        if (!ServerConfig.enableThirst.get())
            return;

        Player player = minecraft.player;

        IThirst thirst = ThirstHelper.getThirst(player);
        int thirstLevel = thirst.getThirst();
        float thirstHydrationLevel = thirst.getHydration();

        // When the update counter isn't incrementing, ensure the same numbers are produced (freezes moving gui elements)
        RANDOM.setSeed(updateCounter * 312871L);

        if (minecraft.gameMode.getPlayerMode().isSurvival())
        {
            drawThirst(guiGraphics, width, height, thirstLevel, thirstHydrationLevel);
            forgeGui.rightHeight += 10;
        }
    }

    private static void drawThirst(GuiGraphics gui, int width, int height, int thirstLevel, float thirstHydrationLevel)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        int left = width / 2 + 91 + ClientConfig.thirstLeftOffset.get();
        int top = height - ((ForgeGui)Minecraft.getInstance().gui).rightHeight + ClientConfig.thirstTopOffset.get();

        for (int i = 0; i < 10; i++)
        {
            int dropletHalf = i * 2 + 1;
            int iconIndex = 0;

            int startX = left - i * 8 - 9;
            int startY = top;

            int backgroundU = 0;

            if (player.hasEffect(TANEffects.THIRST.get()))
            {
                iconIndex += 4;
                backgroundU += 117;
            }

            if (thirstHydrationLevel <= 0.0F && updateCounter % (thirstLevel * 3 + 1) == 0)
            {
                startY = top + (RANDOM.nextInt(3) - 1);
            }

            // Draw the background of each thirst droplet
            gui.blit(OVERLAY, startX, startY, backgroundU, 32, 9, 9);

            // Draw a full droplet
            if (thirstLevel > dropletHalf)
            {
                gui.blit(OVERLAY, startX, startY, (iconIndex + 4) * 9, 32, 9, 9);
            }
            else if (thirstLevel == dropletHalf) // Draw a half droplet
            {
                gui.blit(OVERLAY, startX, startY, (iconIndex + 5) * 9, 32, 9, 9);
            }
        }
    }


    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    private static class OverlayRegister
    {
        @SubscribeEvent
        public static void registerOverlays(RegisterGuiOverlaysEvent event)
        {
            event.registerBelow(VanillaGuiOverlay.AIR_LEVEL.id(), "thirst_level", (forgeGui, guiGraphics, partialTick, screenWidth, screenHeight) ->
            {
                Minecraft minecraft = Minecraft.getInstance();
                if (!minecraft.options.hideGui && forgeGui.shouldDrawSurvivalElements())
                {
                    forgeGui.setupOverlayRenderState(true, false);
                    renderThirst(forgeGui, guiGraphics, partialTick, screenWidth, screenHeight);
                }
            });
        }
    }
}
