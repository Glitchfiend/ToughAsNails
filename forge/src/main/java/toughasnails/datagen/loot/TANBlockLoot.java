/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.datagen.loot;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import toughasnails.api.block.TANBlocks;
import toughasnails.core.ToughAsNails;
import toughasnails.core.ToughAsNailsForge;

import java.util.Map;
import java.util.Set;

public class TANBlockLoot extends BlockLootSubProvider
{
    private static final Set<Item> EXPLOSION_RESISTANT = Set.of();

    public TANBlockLoot()
    {
        super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate()
    {
        this.dropSelf(TANBlocks.TEMPERATURE_GAUGE);
        this.dropSelf(TANBlocks.RAIN_COLLECTOR);
        this.dropSelf(TANBlocks.WATER_PURIFIER);
    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        return BuiltInRegistries.BLOCK.entrySet().stream().filter(e -> e.getKey().location().getNamespace().equals(ToughAsNails.MOD_ID)).map(Map.Entry::getValue).toList();
    }
}
