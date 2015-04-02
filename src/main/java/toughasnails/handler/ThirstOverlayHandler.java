package toughasnails.handler;

import static toughasnails.util.RenderUtils.drawTexturedModalRect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.thirst.ThirstStats;

public class ThirstOverlayHandler
{
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/overlay.png");
    
    private final Minecraft minecraft = Minecraft.getMinecraft();
    
    @SubscribeEvent
    public void onPreRenderOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (event.type == ElementType.AIR)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, -10.0F, 0.0F);
        }
    }
    
    @SubscribeEvent
    public void onPostRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        ScaledResolution resolution = event.resolution;
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        
        ThirstStats thirstStats = (ThirstStats)player.getExtendedProperties("thirst");
        int thirstLevel = thirstStats.getThirstLevel();
        
        if (event.type == ElementType.AIR)
        {
            GlStateManager.popMatrix();
        }
        else if (event.type == ElementType.EXPERIENCE)
        {
            minecraft.getTextureManager().bindTexture(OVERLAY);
            
            drawThirst(width, height, thirstLevel);
        }
    }
    
    private void drawThirst(int width, int height, int thirstLevel)
    {
        int left = width / 2 + 91;
        int top = height - 49;
        
        for (int i = 0; i < 10; i++)
        {
            int dropletHalf = i * 2 + 1;
            int iconIndex = 0;
            int startX = left - i * 8 - 9;
            int startY = top;
            
            drawTexturedModalRect(startX, startY, iconIndex * 8, 16, 9, 9);
            
            if (thirstLevel > dropletHalf)
            {
                drawTexturedModalRect(startX, startY, (iconIndex + 4) * 9, 16, 9, 9);
            }
            if (thirstLevel == dropletHalf)
            {
                drawTexturedModalRect(startX, startY, (iconIndex + 5) * 9, 16, 9, 9);
            }
        }
    }
}
