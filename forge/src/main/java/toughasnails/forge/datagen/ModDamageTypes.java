/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.datagen;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;
import toughasnails.api.damagesource.TANDamageTypes;

public class ModDamageTypes
{
    protected static void bootstrap(BootstrapContext<DamageType> context)
    {
        context.register(TANDamageTypes.HYPERTHERMIA, new DamageType("toughasnails.hyperthermia", 0.1F));
        context.register(TANDamageTypes.THIRST, new DamageType("toughasnails.thirst", 0.0F));
    }
}
