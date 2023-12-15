/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.datagen;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageType;
import toughasnails.api.damagesource.TANDamageTypes;

public class ModDamageTypes
{
    protected static void bootstrap(BootstapContext<DamageType> context)
    {
        context.register(TANDamageTypes.HYPERTHERMIA, new DamageType("hyperthermia", 0.1F));
    }
}
