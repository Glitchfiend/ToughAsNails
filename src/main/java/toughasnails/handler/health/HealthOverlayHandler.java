package toughasnails.handler.health;

import static toughasnails.util.RenderUtils.drawTexturedModalRect;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.HealthHelper;
import toughasnails.config.GameplayOption;
import toughasnails.config.SyncedConfigHandler;

public class HealthOverlayHandler
{
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/overlay.png");
    
    private final Random random = new Random();
    private final Minecraft minecraft = Minecraft.getMinecraft();
    
    @SubscribeEvent
    public void onPostRenderOverlay(RenderGameOverlayEvent.Pre event)
    {
        ScaledResolution resolution = event.getResolution();
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        
        if (event.getType() == ElementType.HEALTH && SyncedConfigHandler.getBooleanValue(GameplayOption.ENABLE_LOWERED_STARTING_HEALTH))
        {
            minecraft.getTextureManager().bindTexture(OVERLAY);

            if (minecraft.playerController.gameIsSurvivalOrAdventure())
            {
                drawInactiveHearts(width, height, HealthHelper.getInactiveHearts(player));
            }
        }
    }
    
    private void drawInactiveHearts(int width, int height, int inactiveHearts)
    {
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        int left = width / 2 - 91;
        int top = height - 39;
        
        for (int i = 0; i < inactiveHearts; i++)
        {
            int activeOffset = 8 * (10 - inactiveHearts);
            int startX = left + i * 8 + activeOffset;
            int startY = top;
             
            drawTexturedModalRect(startX, startY, 0, 43, 9, 9);
        }
        
        minecraft.getTextureManager().bindTexture(Gui.ICONS);
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
    }
}
