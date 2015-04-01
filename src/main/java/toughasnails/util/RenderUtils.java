package toughasnails.util;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

public class RenderUtils
{
    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
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
