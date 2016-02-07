package toughasnails.handler;

import static toughasnails.util.RenderUtils.drawTexturedModalRect;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import org.lwjgl.opengl.GL11;

import toughasnails.temperature.TemperatureInfo;
import toughasnails.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureScale.TemperatureRange;
import toughasnails.temperature.TemperatureStats;

public class TemperatureOverlayHandler
{
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/overlay.png");
    public static final ResourceLocation ICE_VIGNETTE = new ResourceLocation("toughasnails:textures/gui/ice_vignette.png");
    public static final ResourceLocation FIRE_VIGNETTE = new ResourceLocation("toughasnails:textures/gui/fire_vignette.png");
    
    private final Random random = new Random();
    private final Minecraft minecraft = Minecraft.getMinecraft();
    
    private int updateCounter;
    private FlashType flashType = FlashType.INCREASE;
    private int flashCounter = -1;
    private int prevTemperatureLevel = -1;
    
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
        
        TemperatureStats temperatureStats = (TemperatureStats)player.getExtendedProperties("temperature");
        TemperatureInfo temperature = temperatureStats.getTemperature();
        
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
        
        int temperatureLevel = temperature.getScalePos();
        
        if (prevTemperatureLevel == -1) prevTemperatureLevel = temperatureLevel;
        
        if (temperatureLevel > prevTemperatureLevel)
        {
            flashCounter = updateCounter + 16;
            flashType = FlashType.INCREASE;
        }
        else if (temperatureLevel < prevTemperatureLevel)
        {
            flashCounter = updateCounter + 16;
            flashType = FlashType.DECREASE;
        }
        
        prevTemperatureLevel = temperatureLevel;
        
        TemperatureIcon temperatureIcon = getTemperatureIcon(temperatureLevel);
        int updateDelta = flashCounter - updateCounter;
        
        drawTexturedModalRect(left, top, 16 * (temperatureIcon.backgroundIndex), 0, 16, 16);
        drawTexturedModalRect(left, top, 16 * (temperatureIcon.foregroundIndex), 0, 16, 16);
        
        if (temperatureIcon == TemperatureIcon.BALL)
        {
            renderColouredBall(left, top, temperature, 0);
        }
        
        if (flashCounter > (long)updateCounter)
        {
            if (updateDelta > 6 && updateDelta / 3L % 2L == 1L)
            {
                drawTexturedModalRect(left, top, 16 * (temperatureIcon.backgroundIndex + flashType.backgroundShift), 0, 16, 16);
                drawTexturedModalRect(left, top, 16 * (temperatureIcon.foregroundIndex + flashType.foregroundShift), 0, 16, 16);
                
                if (temperatureIcon == TemperatureIcon.BALL)
                {
                    renderColouredBall(left, top, temperature, 2);
                }
            }
            
            GlStateManager.pushMatrix();
            
            if (flashType == FlashType.INCREASE)
            {
                GL11.glTranslatef(left + 16F, top + 16F, 0F);
                GL11.glRotatef(180F, 0F, 0F, 1F);
            }
            else
            {
                GL11.glTranslatef(left, top, 0F);
            }
            
            drawTexturedModalRect(0, 0, 16 * (16 - updateDelta), 16 * 15, 16, 16);
            GlStateManager.popMatrix();
        }
    }
    
    private void renderColouredBall(int x, int y, TemperatureInfo temperature, int textureShift)
    {
        TemperatureRange temperatureRange = temperature.getTemperatureRange();
        float changeDelta = temperature.getRelativeScaleDelta();
        
        if (temperatureRange != TemperatureRange.MILD)
        {
            boolean coolBall = temperatureRange == TemperatureRange.COOL;
            float ballDelta = coolBall ? 1.0F - changeDelta : changeDelta;

            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, ballDelta);
            drawTexturedModalRect(x, y, 16 * ((coolBall ? 8 : 9) + textureShift), 16 * 1, 16, 16);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
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
            WorldRenderer renderer = tessellator.getWorldRenderer();
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            renderer.pos(0.0D, height, -90.0D).tex(0.0D, 1.0D).endVertex();
            renderer.pos(width, height, -90.0D).tex(1.0D, 1.0D).endVertex();
            renderer.pos(width, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
            renderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
    
    private static TemperatureIcon getTemperatureIcon(int scalePos)
    {
        if (scalePos < 0 || scalePos > TemperatureScale.getScaleTotal())
        {
            return null;
        }
        
        TemperatureIcon temperatureIcon = null;
        
        for (int index = 0; index < TemperatureIcon.values().length; index++)
        {
            temperatureIcon = TemperatureIcon.values()[index];
            
            if (TemperatureScale.isScalePosInRange(scalePos, temperatureIcon.startRange, temperatureIcon.endRange))
            {
                break;
            }
        }
        
        return temperatureIcon;
    }
    
    private static enum TemperatureIcon
    {
        SNOWFLAKE(0, 9, TemperatureRange.ICY),
        BALL(1, 10, TemperatureRange.COOL, TemperatureRange.WARM),
        FIRE(2, 11, TemperatureRange.HOT);
        
        public final int backgroundIndex;
        public final int foregroundIndex;
        public final TemperatureRange startRange;
        public final TemperatureRange endRange;
        
        private TemperatureIcon(int backgroundIndex, int foregroundIndex, TemperatureRange startRange, TemperatureRange endRange)
        {
            this.backgroundIndex = backgroundIndex;
            this.foregroundIndex = foregroundIndex;
            this.startRange = startRange;
            this.endRange = endRange;
        }
        
        private TemperatureIcon(int backgroundIndex, int foregroundIndex, TemperatureRange range)
        {
            this(backgroundIndex, foregroundIndex, range, range);
        }
    }
    
    private static enum FlashType
    {
        INCREASE(3, 3), 
        DECREASE(3, 3);
        
        public final int backgroundShift;
        public final int foregroundShift;
        
        private FlashType(int backgroundShift, int foregroundShift)
        {
            this.backgroundShift = backgroundShift;
            this.foregroundShift = foregroundShift;
        }
    }
}
