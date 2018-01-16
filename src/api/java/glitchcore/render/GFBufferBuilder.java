/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.render;

import net.minecraft.client.renderer.BufferBuilder;

public class GFBufferBuilder extends BufferBuilder
{
    public GFBufferBuilder(int bufferSize) { super(bufferSize); };


    @Override
    public BufferBuilder tex(double u, double v)
    {
        return this.gfTex(u, v);
    }

    public GFBufferBuilder gfTex(double u, double v)
    {
        super.tex(u, v);
        return this;
    }

    @Override
    public BufferBuilder lightmap(int p_187314_1_, int p_187314_2_)
    {
        return this.gfLightmap(p_187314_1_, p_187314_2_);
    }

    public GFBufferBuilder gfLightmap(int p_187314_1_, int p_187314_2_)
    {
        super.lightmap(p_187314_1_, p_187314_2_);
        return this;
    }

    @Override
    public BufferBuilder color(float red, float green, float blue, float alpha)
    {
        return this.gfColor(red, green, blue, alpha);
    }

    public GFBufferBuilder gfColor(float red, float green, float blue, float alpha)
    {
        super.color(red, green, blue, alpha);
        return this;
    }

    @Override
    public BufferBuilder color(int red, int green, int blue, int alpha)
    {
        return this.gfColor(red, green, blue, alpha);
    }

    public GFBufferBuilder gfColor(int red, int green, int blue, int alpha)
    {
        super.color(red, green, blue, alpha);
        return this;
    }

    @Override
    public BufferBuilder pos(double x, double y, double z)
    {
        return this.gfPos(x, y, z);
    }

    public GFBufferBuilder gfPos(double x, double y, double z)
    {
        super.pos(x, y, z);
        return this;
    }

    @Override
    public BufferBuilder normal(float x, float y, float z)
    {
        return this.gfNormal(x, y, z);
    }

    public GFBufferBuilder gfNormal(float x, float y, float z)
    {
        super.normal(x, y, z);
        return this;
    }
}
