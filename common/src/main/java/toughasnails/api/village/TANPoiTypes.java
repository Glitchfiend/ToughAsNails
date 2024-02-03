/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.village;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import toughasnails.api.TANAPI;

public class TANPoiTypes
{
    public static final ResourceKey<PoiType> CLIMATOLOGIST = createKey("climatologist");

    private static ResourceKey<PoiType> createKey(String name)
    {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(TANAPI.MOD_ID, name));
    }
}
