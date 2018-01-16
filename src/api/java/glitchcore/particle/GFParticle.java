/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.particle;

import glitchcore.render.GFBufferBuilder;
import glitchcore.render.GFTessellator;
import glitchcore.render.GFWrappedBufferBuilder;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class GFParticle extends Particle
{
    protected GFParticle(World world, double x, double y, double z) { super(world, x, y, z); }
    public GFParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) { super(world, x, y, z, xSpeed, ySpeed, zSpeed); }

    public void renderParticle(GFWrappedBufferBuilder buffer, Entity entity, float partialTicks, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY)
    {
        super.renderParticle(buffer.buffer, entity, partialTicks, rotX, rotXZ, rotZ, rotYZ, rotXY);
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY)
    {
        this.renderParticle(new GFWrappedBufferBuilder(buffer), entity, partialTicks, rotX, rotXZ, rotZ, rotYZ, rotXY);
    }
}
