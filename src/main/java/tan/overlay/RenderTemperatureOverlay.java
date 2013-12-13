package tan.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import tan.api.PlayerStatRegistry;
import tan.configuration.TANConfigurationTemperature;
import tan.stats.TemperatureStat;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderTemperatureOverlay extends RenderTANOverlay
{
    public float temperature;
    public int iTemperature;
    
    public ResourceLocation overlayLocation = new ResourceLocation("toughasnails:textures/overlay/overlay.png");
    public ResourceLocation vignetteFreezingLocation = new ResourceLocation("toughasnails:textures/overlay/freezingVignette.png");
    public ResourceLocation vignetteBurningLocation = new ResourceLocation("toughasnails:textures/overlay/burningVignette.png");
    
    public float prevVignetteBrightness = 1.0F;
    
    @Override
    public void preRender(RenderGameOverlayEvent.Pre event)
    {
        temperature = tanData.getFloat(PlayerStatRegistry.getStatName(TemperatureStat.class));
        iTemperature = MathHelper.floor_float(temperature);
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        bindTexture(overlayLocation);
        {
            if (!minecraft.thePlayer.capabilities.isCreativeMode)
            {
                renderTemperature();
            }
        }
        bindTexture(new ResourceLocation("minecraft:textures/gui/icons.png"));

        if (!minecraft.thePlayer.capabilities.isCreativeMode)
        {
            if (temperature > 44F)
            {
                float brightness = (47F - temperature) / 3F;
                
                renderVignette(vignetteBurningLocation, brightness, scaledRes.getScaledWidth(), scaledRes.getScaledHeight());
            }
            else if (temperature < 30F)
            {
                float brightness = 1.0F - (-(temperature - 30F) / 3);
                
                renderVignette(vignetteFreezingLocation, brightness, scaledRes.getScaledWidth(), scaledRes.getScaledHeight());
            }
        }
    }
    
    private void renderTemperature()
    {
        int displayTemperature = TemperatureStat.getConvertedDisplayTemperature(iTemperature);
        
        String temperatureSymbol = TemperatureStat.getTemperatureSymbol();
        String temperatureType = TANConfigurationTemperature.temperatureType;

        int temperatureXPos = scaledRes.getScaledWidth() / 2 - 8;
        int temperatureYPos = scaledRes.getScaledHeight() - 52;
        
        minecraft.mcProfiler.startSection("temperatureBall");
        {   
            this.drawTexturedModalRect(temperatureXPos, temperatureYPos, 16, 0, 16, 16);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, iTemperature / 20F - 1.35F);
            this.drawTexturedModalRect(temperatureXPos, temperatureYPos, 0, 0, 16, 16);
        }
        minecraft.mcProfiler.endSection();
        
        int guiSize = Minecraft.getMinecraft().gameSettings.guiScale;
        
        float textScale = 0.75F;
        int xOffset = 0;
        int yOffset = 0;
        
        if (guiSize == 0)
        {
            xOffset = -1;
            textScale = 0.75F;
        }
        if (guiSize == 1)
        {
            textScale = 0.45F;
        }
        if (guiSize == 2)
        {
            xOffset = 1;
            textScale = 0.5F;
        }
        if (guiSize == 3)
        {
            textScale = 0.65F;
        }
        
        minecraft.mcProfiler.startSection("temperatureLevel");
        {
            GL11.glPushMatrix();
            {
                String text = displayTemperature + temperatureSymbol;
                
                GL11.glTranslatef((float)(temperatureXPos - (fontRenderer.getStringWidth(text) / 2) + 12 + xOffset), (float)(temperatureYPos + 7 + yOffset), 0.0F);
                GL11.glScalef(textScale, textScale, 0.0F);
                
                drawStringWithBorder(fontRenderer, text, 0, 0, 0, 16777215);
            }
            GL11.glPopMatrix();
        }
        minecraft.mcProfiler.endSection();
    }
    
    private void renderVignette(ResourceLocation texture, float brightness, int width, int height)
    {
        GL11.glEnable(GL11.GL_BLEND);
        
        brightness = 1.0F - brightness;

        if (brightness < 0.0F)
        {
            brightness = 0.0F;
        }

        if (brightness > 1.0F)
        {
            brightness = 1.0F;
        }

        this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(brightness - this.prevVignetteBrightness) * 0.01D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
        GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
        bindTexture(texture);
        {
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(0.0D, (double)height, -90.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double)width, (double)height, -90.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double)width, 0.0D, -90.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
            tessellator.draw();
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
        bindTexture(new ResourceLocation("minecraft:textures/gui/icons.png"));
    }
}
