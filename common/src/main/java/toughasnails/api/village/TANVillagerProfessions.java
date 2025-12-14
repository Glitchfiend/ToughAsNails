/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.village;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import toughasnails.core.ToughAsNails;

public class TANVillagerProfessions
{
    public static final ResourceKey<VillagerProfession> CLIMATOLOGIST = createKey("climatologist");

    private static ResourceKey<VillagerProfession> createKey(String name)
    {
        return ResourceKey.create(Registries.VILLAGER_PROFESSION, Identifier.fromNamespaceAndPath(ToughAsNails.MOD_ID, name));
    }
}
