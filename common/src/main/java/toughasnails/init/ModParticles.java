/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import toughasnails.api.TANAPI;

import java.util.function.BiConsumer;

public class ModParticles
{
    public static void registerParticles(BiConsumer<ResourceLocation, ParticleType<?>> func)
    {
    }

    private static ParticleType<?> register(BiConsumer<ResourceLocation, ParticleType<?>> func, String name, ParticleType<?> particle)
    {
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), particle);
        return particle;
    }
}
