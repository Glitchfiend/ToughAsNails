/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.event.client.RegisterParticleSpritesEvent;
import toughasnails.api.particle.TANParticles;
import toughasnails.client.particle.ThermoregulatorParticle;

public class ModClient
{
    public static void registerParticleSprites(RegisterParticleSpritesEvent event)
    {
        event.registerSpriteSet(TANParticles.THERMOREGULATOR_COOL, ThermoregulatorParticle.Provider::new);
        event.registerSpriteSet(TANParticles.THERMOREGULATOR_WARM, ThermoregulatorParticle.Provider::new);
        event.registerSpriteSet(TANParticles.THERMOREGULATOR_NEUTRAL, ThermoregulatorParticle.Provider::new);
    }

    public static void setupRenderTypes()
    {
    }
}
