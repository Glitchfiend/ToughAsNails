package toughasnails.handler;

import static toughasnails.util.RenderUtils.drawTexturedModalRect;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.HealthHelper;

public class HealthOverlayHandler
{
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/overlay.png");
    
    private final Random random = new Random();
    private final Minecraft minecraft = Minecraft.getMinecraft();
    
    @SubscribeEvent
    public void onPostRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        ScaledResolution resolution = event.getResolution();
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        
       if (event.getType() == ElementType.EXPERIENCE)
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
        int left = width / 2 - 91;
        int top = height - 39;
        
        for (int i = 0; i < inactiveHearts; i++)
        {
            int activeOffset = 8 * (10 - inactiveHearts);
            int startX = left + i * 8 + activeOffset;
            int startY = top;
             
            drawTexturedModalRect(startX, startY, 0, 43, 9, 9);
        }
    }
}
