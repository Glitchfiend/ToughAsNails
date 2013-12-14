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
    
    @Override
    public void preRender(RenderGameOverlayEvent.Pre event)
    {
        //Check for crosshairs since they are always drawn and is before the air bar
        if (event.type != ElementType.CROSSHAIRS) 
        {
            return;
        }
        
        temperature = tanData.getFloat(PlayerStatRegistry.getStatName(TemperatureStat.class));
        iTemperature = MathHelper.floor_float(temperature);

        bindTexture(overlayLocation);
        {
            if (!minecraft.thePlayer.capabilities.isCreativeMode)
            {
                renderTemperature();
            }
        }
        bindTexture(new ResourceLocation("minecraft:textures/gui/icons.png"));
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
}
