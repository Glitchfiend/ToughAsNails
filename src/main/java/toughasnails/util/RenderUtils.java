package toughasnails.util;

import net.minecraft.client.renderer.BufferBuilder;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderUtils
{
    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos((double)(x + 0), (double)(y + height), 0.0D).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).endVertex();;
        buffer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        buffer.pos((double)(x + width), (double)(y + 0), 0.0D).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        buffer.pos((double)(x + 0), (double)(y + 0), 0.0D).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }
}
