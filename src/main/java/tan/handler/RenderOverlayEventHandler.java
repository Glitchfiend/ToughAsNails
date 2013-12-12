package tan.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import tan.api.PlayerStatRegistry;
import tan.configuration.TANConfigurationTemperature;
import tan.stats.TemperatureStat;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderOverlayEventHandler
{
    public Minecraft minecraft = Minecraft.getMinecraft();
    public FontRenderer fontRenderer = minecraft.fontRenderer;
    public ScaledResolution scaledRes;
    public NBTTagCompound tanData;
    public float temperature;
    public int iTemperature;
    
    public ResourceLocation overlayLocation = new ResourceLocation("toughasnails:textures/overlay/overlay.png");
    public ResourceLocation vignetteFreezingLocation = new ResourceLocation("toughasnails:textures/overlay/freezingVignette.png");
    public ResourceLocation vignetteBurningLocation = new ResourceLocation("toughasnails:textures/overlay/burningVignette.png");
    
    public float prevVignetteBrightness = 1.0F;
    
    @ForgeSubscribe
    public void render(RenderGameOverlayEvent.Pre event)
    {
        scaledRes = event.resolution;
        tanData = minecraft.thePlayer.getEntityData().getCompoundTag("ToughAsNails");
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
            if (temperature >= 43F)
            {
                renderVignette(vignetteBurningLocation, (47F - temperature) / 5F, scaledRes.getScaledWidth(), scaledRes.getScaledHeight());
            }
            else if (temperature <= 31F)
            {
                renderVignette(vignetteFreezingLocation, -(27F - temperature) / 5F, scaledRes.getScaledWidth(), scaledRes.getScaledHeight());
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
        
        minecraft.mcProfiler.startSection("temperatureLevel");
        {
            GL11.glPushMatrix();
            {
                String text = displayTemperature + temperatureSymbol;
                
                GL11.glTranslatef((float)(temperatureXPos - (fontRenderer.getStringWidth(text) / 2) + 12), (float)(temperatureYPos + 6), 0.0F);
                GL11.glScalef(0.65F, 0.65F, 0.0F);
                
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

    public static void bindTexture(ResourceLocation resourceLocation)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(resourceLocation);
    }
    
    public void drawStringWithBorder(FontRenderer fontrenderer, String string, int x, int y, int borderColour, int colour)
    {
        fontrenderer.drawString(string, x + 1, y, borderColour);
        fontrenderer.drawString(string, x - 1, y, borderColour);
        fontrenderer.drawString(string, x, y + 1, borderColour);
        fontrenderer.drawString(string, x, y - 1, borderColour);
        fontrenderer.drawString(string, x, y, colour);
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, 0.0, (u + 0) * f, (v + height) * f1);
        tessellator.addVertexWithUV(x + width, y + height, 0.0, (u + width) * f, (v + height) * f1);
        tessellator.addVertexWithUV(x + width, y + 0, 0.0, (u + width) * f, (v + 0) * f1);
        tessellator.addVertexWithUV(x + 0, y + 0, 0.0, (u + 0) * f, (v + 0) * f1);
        tessellator.draw();
    }
}
