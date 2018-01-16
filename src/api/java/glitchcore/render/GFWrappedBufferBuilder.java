/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.render;

import net.minecraft.client.renderer.BufferBuilder;

public class GFWrappedBufferBuilder
{
    public final BufferBuilder buffer;

    public GFWrappedBufferBuilder(BufferBuilder buffer)
    {
        this.buffer = buffer;
    }

    public void tex(double u, double v)
    {
        buffer.tex(u, v);
    }
    public void lightmap(int p_187314_1_, int p_187314_2_)
    {
        buffer.lightmap(p_187314_1_, p_187314_2_);
    }

    public void color(float red, float green, float blue, float alpha)
    {
        buffer.color(red, green, blue, alpha);
    }

    public void color(int red, int green, int blue, int alpha)
    {
        buffer.color(red, green, blue, alpha);
    }

    public void pos(double x, double y, double z)
    {
        buffer.pos(x, y, z);
    }

    public void normal(float x, float y, float z)
    {
        buffer.normal(x, y, z);
    }
}
