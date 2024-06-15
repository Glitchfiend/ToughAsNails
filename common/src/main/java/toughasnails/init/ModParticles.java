/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import toughasnails.api.TANAPI;
import toughasnails.api.particle.TANParticles;

import java.util.function.BiConsumer;

public class ModParticles
{
    public static void registerParticles(BiConsumer<ResourceLocation, ParticleType<?>> func)
    {
        TANParticles.THERMOREGULATOR_COOL = register(func, "thermoregulator_cool", new SimpleParticleType(false));
        TANParticles.THERMOREGULATOR_WARM = register(func, "thermoregulator_warm", new SimpleParticleType(false));
        TANParticles.THERMOREGULATOR_NEUTRAL = register(func, "thermoregulator_neutral", new SimpleParticleType(false));
    }

    private static <T extends ParticleType<? extends ParticleOptions>> T register(BiConsumer<ResourceLocation, ParticleType<?>> func, String name, T particle)
    {
        func.accept(ResourceLocation.fromNamespaceAndPath(TANAPI.MOD_ID, name), particle);
        return particle;
    }
}
