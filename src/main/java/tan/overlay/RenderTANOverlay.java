package tan.overlay;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ForgeSubscribe;

public abstract class RenderTANOverlay
{
    public Minecraft minecraft = Minecraft.getMinecraft();
    public FontRenderer fontRenderer = minecraft.fontRenderer;
    public ScaledResolution scaledRes;
    public NBTTagCompound tanData;
    
    @ForgeSubscribe
    public void render(RenderGameOverlayEvent.Pre event)
    {
        scaledRes = event.resolution;
        tanData = minecraft.thePlayer.getEntityData().getCompoundTag("ToughAsNails");
        
        preRender(event);
    }

    abstract void preRender(RenderGameOverlayEvent.Pre event);
    
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
