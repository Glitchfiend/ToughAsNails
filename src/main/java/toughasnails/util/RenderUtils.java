package toughasnails.util;

import glitchcore.render.GFBufferBuilder;
import glitchcore.render.GFTessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class RenderUtils
{
    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        GFTessellator tessellator = GFTessellator.getInstance();
        GFBufferBuilder buffer = tessellator.getBufferBuilder();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.gfPos((double)(x + 0), (double)(y + height), 0.0D).gfTex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).endVertex();;
        buffer.gfPos((double)(x + width), (double)(y + height), 0.0D).gfTex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        buffer.gfPos((double)(x + width), (double)(y + 0), 0.0D).gfTex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        buffer.gfPos((double)(x + 0), (double)(y + 0), 0.0D).gfTex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }
}
