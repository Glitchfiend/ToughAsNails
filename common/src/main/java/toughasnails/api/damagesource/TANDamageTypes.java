/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import toughasnails.api.TANAPI;

public class TANDamageTypes
{
    public static final ResourceKey<DamageType> HYPERTHERMIA = register("hyperthermia");

    private static ResourceKey<DamageType> register(String name)
    {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(TANAPI.MOD_ID, name));
    }
}
