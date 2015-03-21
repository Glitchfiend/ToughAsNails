package toughasnails.handler;

import java.util.Random;

import toughasnails.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureScale.TemperatureRange;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class TemperatureOverlayEventHandler
{
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/overlay.png");
    
    private final Random random = new Random();
    private int updateCounter;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == Phase.END)
        {
            updateCounter++;
        }
    }
    
    @SubscribeEvent
    public void onPostRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        if (event.type != ElementType.EXPERIENCE) return;
        
        ScaledResolution resolution = event.resolution;
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        Minecraft minecraft = Minecraft.getMinecraft();
        
        minecraft.getTextureManager().bindTexture(OVERLAY);
        
        if (minecraft.playerController.gameIsSurvivalOrAdventure())
        {
            drawTemperature(width, height);
        }
    }
    
    private void drawTemperature(int width, int height)
    {
        int left = width / 2 - 8;
        int top = height - 52; 
        
        int temperature = 35;
        TemperatureRange temperatureRange = TemperatureScale.getTemperatureRange(temperature);
        
        float changeDelta = (float)TemperatureScale.getRelativeScaleDelta(temperature) / (float)temperature;
        
        if (temperatureRange == TemperatureRange.values()[0] || temperatureRange == TemperatureRange.values()[TemperatureRange.values().length - 1])
        {
            top += (random.nextInt(4) - 2);
            left += (random.nextInt(4) - 2);
        }
        else if (changeDelta <= 0.25F || changeDelta >= 0.75F)
        {
            if ((updateCounter % 200) == 0)
            {
                top += (random.nextInt(4) - 2);
            }
        }
        
        drawTexturedModalRect(left, top, 16 * temperatureRange.ordinal(), 0, 16, 16);
    }
    
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV((double)(x + 0), (double)(y + height), 0.0D, (double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1));
        worldrenderer.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, (double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1));
        worldrenderer.addVertexWithUV((double)(x + width), (double)(y + 0), 0.0D, (double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1));
        worldrenderer.addVertexWithUV((double)(x + 0), (double)(y + 0), 0.0D, (double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1));
        tessellator.draw();
    }
}
