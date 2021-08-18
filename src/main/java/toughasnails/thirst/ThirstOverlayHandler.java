/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.thirst;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmlclient.gui.GuiUtils;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.api.thirst.IThirst;
import toughasnails.config.ClientConfig;
import toughasnails.config.ServerConfig;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ThirstOverlayHandler
{
    private static final Random RANDOM = new Random();
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/icons.png");

    public static final IIngameOverlay THIRST_LEVEL_ELEMENT = OverlayRegistry.registerOverlayTop("Thirst Level", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements())
        {
            gui.setupOverlayRenderState(true, false);
            renderThirst(gui, mStack, partialTicks, screenWidth, screenHeight);
        }
    });

    private static int updateCounter;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft minecraft = Minecraft.getInstance();

        if (ServerConfig.enableThirst.get() && event.phase == TickEvent.Phase.END && !minecraft.isPaused())
        {
            updateCounter++;
        }
    }

    private static void renderThirst(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height)
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
            RenderSystem.setShaderTexture(0, OVERLAY);
            drawThirst(mStack, width, height, thirstLevel, thirstHydrationLevel);
            gui.right_height += 10;
            RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
        }
    }

    private static void drawThirst(PoseStack matrixStack, int width, int height, int thirstLevel, float thirstHydrationLevel)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        int left = width / 2 + 91 + ClientConfig.thirstLeftOffset.get();
        int top = height - ((ForgeIngameGui)Minecraft.getInstance().gui).right_height + ClientConfig.thirstTopOffset.get();

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
            GuiUtils.drawTexturedModalRect(matrixStack, startX, startY, backgroundU, 32, 9, 9, 9);

            // Draw a full droplet
            if (thirstLevel > dropletHalf)
            {
                GuiUtils.drawTexturedModalRect(matrixStack, startX, startY, (iconIndex + 4) * 9, 32, 9, 9, 9);
            }
            else if (thirstLevel == dropletHalf) // Draw a half droplet
            {
                GuiUtils.drawTexturedModalRect(matrixStack, startX, startY, (iconIndex + 5) * 9, 32, 9, 9, 9);
            }
        }
    }
}
