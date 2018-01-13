/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;

public class GFTessellator extends Tessellator
{
    private final GFBufferBuilder bufferBuilder;
    private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();

    private static final GFTessellator INSTANCE = new GFTessellator(2097152);

    public GFTessellator(int bufferSize)
    {
        super(bufferSize);

        this.bufferBuilder = new GFBufferBuilder(bufferSize);
    }

    public static GFTessellator getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void draw()
    {
        this.bufferBuilder.finishDrawing();
        this.vboUploader.draw(this.bufferBuilder);
    }

    public GFBufferBuilder getBufferBuilder()
    {
        return this.bufferBuilder;
    }

    @Override
    public BufferBuilder getBuffer()
    {
        return this.getBufferBuilder();
    }
}
