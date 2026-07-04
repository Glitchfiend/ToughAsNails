/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.event.client.RegisterColorsEvent;
import glitchcore.event.client.RegisterParticleSpritesEvent;
import net.minecraft.client.color.block.BlockTintSources;
import toughasnails.api.particle.TANParticles;
import toughasnails.client.particle.ThermoregulatorParticle;

import java.util.List;

import static toughasnails.api.block.TANBlocks.RAIN_COLLECTOR;
import static toughasnails.api.block.TANBlocks.WATER_PURIFIER;

public class ModClient
{
    public static void registerBlockColors(RegisterColorsEvent.Block event)
    {
        event.register(List.of(BlockTintSources.constant(0xFF47DAFF)), RAIN_COLLECTOR);
        event.register(List.of(BlockTintSources.constant(0xFF3F76E4)), WATER_PURIFIER);
    }

    public static void registerParticleSprites(RegisterParticleSpritesEvent event)
    {
        event.registerSpriteSet(TANParticles.THERMOREGULATOR_COOL, ThermoregulatorParticle.Provider::new);
        event.registerSpriteSet(TANParticles.THERMOREGULATOR_WARM, ThermoregulatorParticle.Provider::new);
        event.registerSpriteSet(TANParticles.THERMOREGULATOR_NEUTRAL, ThermoregulatorParticle.Provider::new);
    }
}
