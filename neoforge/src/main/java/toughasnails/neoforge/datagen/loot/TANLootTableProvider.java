/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.neoforge.datagen.loot;

import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class TANLootTableProvider
{
    public static SingleRegistryBootstrap<LootTable> create()
    {
        return new LootTableProvider(Set.of(), List.of(new LootTableProvider.SubProviderEntry(TANBlockLoot::new, LootContextParamSets.BLOCK)));
    }
}
