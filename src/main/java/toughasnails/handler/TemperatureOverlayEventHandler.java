package toughasnails.handler;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import org.lwjgl.opengl.GL11;

import toughasnails.temperature.TemperatureInfo;
import toughasnails.temperature.TemperatureScale.TemperatureRange;
import toughasnails.temperature.TemperatureStats;

public class TemperatureOverlayEventHandler
{
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/overlay.png");
    public static final ResourceLocation ICE_VIGNETTE = new ResourceLocation("toughasnails:textures/gui/ice_vignette.png");
    public static final ResourceLocation FIRE_VIGNETTE = new ResourceLocation("toughasnails:textures/gui/fire_vignette.png");
    
    private final Random random = new Random();
    private final Minecraft minecraft = Minecraft.getMinecraft();
    
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
        ScaledResolution resolution = event.resolution;
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        
        TemperatureInfo temperature = ((TemperatureStats)player.getExtendedProperties("temperature")).getTemperature();
        
        if (event.type == ElementType.PORTAL)
        {
            drawTemperatureVignettes(width, height, temperature);
        }
        else if (event.type == ElementType.EXPERIENCE)
        {
            minecraft.getTextureManager().bindTexture(OVERLAY);

            if (minecraft.playerController.gameIsSurvivalOrAdventure())
            {
                drawTemperature(width, height, temperature);
            }
        }
    }
    
    private void drawTemperature(int width, int height, TemperatureInfo temperature)
    {
        int left = width / 2 - 8;
        int top = height - 52; 
        
        TemperatureRange temperatureRange = temperature.getTemperatureRange();
        float changeDelta = temperature.getRelativeScaleDelta();
        
        if (temperatureRange == TemperatureRange.ICY || temperatureRange == TemperatureRange.HOT)
        {
            float shakeDelta = temperatureRange == TemperatureRange.ICY ? 1.0F - changeDelta : changeDelta;
            
            if ((updateCounter % 1) == 0)
            {
                top += (int)((random.nextInt(3) - 1) * Math.min(shakeDelta * 3F, 1.0));
                left += (int)((random.nextInt(3) - 1) * Math.min(shakeDelta * 1.5F, 1.0));
            }
        }
        else if (changeDelta <= 0.25F || changeDelta >= 0.75F)
        {
            if ((updateCounter % 100) == 0)
            {
                top += (random.nextInt(4) - 2);
            }
        }
        
        drawTexturedModalRect(left, top, 16 * temperatureRange.ordinal(), 0, 16, 16);
    }
    
    private void drawTemperatureVignettes(int width, int height, TemperatureInfo temperature)
    {
        TemperatureRange temperatureRange = temperature.getTemperatureRange();
        float opacityDelta = temperature.getRelativeScaleDelta();
        
        ResourceLocation vignetteLocation = null;
        
        if (temperatureRange == TemperatureRange.ICY)
        {
            opacityDelta = 1.0F - opacityDelta;
            vignetteLocation = ICE_VIGNETTE;
        }
        else if (temperatureRange == TemperatureRange.HOT)
        {
            vignetteLocation = FIRE_VIGNETTE;
        }

        if (vignetteLocation != null)
        {
            minecraft.getTextureManager().bindTexture(vignetteLocation);

            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, opacityDelta);
            GlStateManager.disableAlpha();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.startDrawingQuads();
            worldrenderer.addVertexWithUV(0.0D, (double)height, -90.0D, 0.0D, 1.0D);
            worldrenderer.addVertexWithUV((double)width, (double)height, -90.0D, 1.0D, 1.0D);
            worldrenderer.addVertexWithUV((double)width, 0.0D, -90.0D, 1.0D, 0.0D);
            worldrenderer.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
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
