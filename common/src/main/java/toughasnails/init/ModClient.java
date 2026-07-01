/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.event.client.RegisterColorsEvent;
import glitchcore.event.client.RegisterParticleSpritesEvent;
import glitchcore.util.RenderHelper;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import toughasnails.api.particle.TANParticles;
import toughasnails.client.particle.ThermoregulatorParticle;

import static toughasnails.api.block.TANBlocks.RAIN_COLLECTOR;
import static toughasnails.api.block.TANBlocks.WATER_PURIFIER;

public class ModClient
{
    public static void registerBlockColors(RegisterColorsEvent.Block event)
    {
        event.register((state, world, pos, tintIndex) -> 0x47DAFF, RAIN_COLLECTOR);
        event.register((state, world, pos, tintIndex) -> 0x3F76E4, WATER_PURIFIER);
    }

    public static void registerParticleSprites(RegisterParticleSpritesEvent event)
    {
        event.registerSpriteSet(TANParticles.THERMOREGULATOR_COOL, ThermoregulatorParticle.Provider::new);
        event.registerSpriteSet(TANParticles.THERMOREGULATOR_WARM, ThermoregulatorParticle.Provider::new);
        event.registerSpriteSet(TANParticles.THERMOREGULATOR_NEUTRAL, ThermoregulatorParticle.Provider::new);
    }

    public static void setupRenderTypes()
    {
        RenderHelper.setRenderType(RAIN_COLLECTOR, ChunkSectionLayer.CUTOUT);
        RenderHelper.setRenderType(WATER_PURIFIER, ChunkSectionLayer.CUTOUT);
    }
}
