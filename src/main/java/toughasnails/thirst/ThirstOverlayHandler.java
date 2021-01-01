/*******************************************************************************
 * Copyright 2020, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.thirst;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import toughasnails.api.ThirstHelper;
import toughasnails.api.capability.IThirst;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ThirstOverlayHandler
{
    private static final Random RANDOM = new Random();
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/icons.png");

    private int updateCounter;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft minecraft = Minecraft.getInstance();

        if (event.phase == TickEvent.Phase.END && !minecraft.isPaused())
        {
            updateCounter++;
        }
    }

    @SubscribeEvent
    public void onPreRenderOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.AIR)
        {
            Minecraft minecraft = Minecraft.getInstance();
            PlayerEntity player = minecraft.player;
            int width = event.getWindow().getGuiScaledWidth();
            int height = event.getWindow().getGuiScaledHeight();

            IThirst thirst = ThirstHelper.getThirst(player);
            int thirstLevel = thirst.getThirst();
            float thirstHydrationLevel = thirst.getHydration();

            // When the update counter isn't incrementing, ensure the same numbers are produced (freezes moving gui elements)
            RANDOM.setSeed(updateCounter * 312871L);

            if (minecraft.gameMode.getPlayerMode().isSurvival())
            {
                minecraft.getTextureManager().bind(OVERLAY);
                drawThirst(event.getMatrixStack(), width, height, thirstLevel, thirstHydrationLevel);
                ForgeIngameGui.right_height += 10;
                minecraft.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
            }
        }
    }

    private void drawThirst(MatrixStack matrixStack, int width, int height, int thirstLevel, float thirstHydrationLevel)
    {
        int left = width / 2 + 91;
        int top = height - ForgeIngameGui.right_height;

        for (int i = 0; i < 10; i++)
        {
            int dropletHalf = i * 2 + 1;
            int iconIndex = 0;

            int startX = left - i * 8 - 9;
            int startY = top;

            int backgroundU = 0;

            if (false)//minecraft.player.isPotionActive(TANPotions.thirst))
            {
                iconIndex += 4;
                backgroundU += 117;
            }

            if (thirstHydrationLevel <= 0.0F && updateCounter % (thirstLevel * 3 + 1) == 0)
            {
                startY = top + (RANDOM.nextInt(3) - 1);
            }

            // Draw the background of each thirst droplet
            GuiUtils.drawTexturedModalRect(matrixStack, startX, startY, backgroundU, 0, 9, 9, 9);

            // Draw a full droplet
            if (thirstLevel > dropletHalf)
            {
                GuiUtils.drawTexturedModalRect(matrixStack, startX, startY, (iconIndex + 4) * 9, 0, 9, 9, 9);
            }
            else if (thirstLevel == dropletHalf) // Draw a half droplet
            {
                GuiUtils.drawTexturedModalRect(matrixStack, startX, startY, (iconIndex + 5) * 9, 0, 9, 9, 9);
            }
        }
    }
}
