/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import toughasnails.core.ToughAsNailsForge;
import toughasnails.init.ModTags;

import java.util.concurrent.CompletableFuture;

public class TANBiomeTagsProvider extends BiomeTagsProvider
{
    public TANBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, ToughAsNailsForge.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        // Temperature tags
        this.tag(ModTags.Biomes.ICY_BIOMES).add(Biomes.ICE_SPIKES, Biomes.SNOWY_TAIGA, Biomes.SNOWY_PLAINS, Biomes.SNOWY_SLOPES, Biomes.GROVE, Biomes.JAGGED_PEAKS, Biomes.FROZEN_PEAKS, Biomes.SNOWY_BEACH, Biomes.FROZEN_RIVER, Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
        this.tag(ModTags.Biomes.COLD_BIOMES).add(Biomes.THE_END, Biomes.SMALL_END_ISLANDS, Biomes.END_MIDLANDS, Biomes.END_HIGHLANDS, Biomes.END_BARRENS, Biomes.DEEP_DARK, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.MEADOW, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.COLD_OCEAN, Biomes.DEEP_COLD_OCEAN);
        this.tag(ModTags.Biomes.NEUTRAL_BIOMES).add(Biomes.THE_VOID, Biomes.DRIPSTONE_CAVES, Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.DARK_FOREST, Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.CHERRY_GROVE, Biomes.STONY_PEAKS, Biomes.STONY_SHORE, Biomes.BEACH, Biomes.RIVER, Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN);
        this.tag(ModTags.Biomes.WARM_BIOMES).add(Biomes.SOUL_SAND_VALLEY, Biomes.LUSH_CAVES, Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA, Biomes.WARM_OCEAN, Biomes.MUSHROOM_FIELDS);
        this.tag(ModTags.Biomes.HOT_BIOMES).add(Biomes.NETHER_WASTES, Biomes.CRIMSON_FOREST, Biomes.WARPED_FOREST, Biomes.BASALT_DELTAS, Biomes.DESERT, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.ERODED_BADLANDS);

        // Water purity tags
        this.tag(ModTags.Biomes.DIRTY_WATER_BIOMES).add(Biomes.MANGROVE_SWAMP, Biomes.MUSHROOM_FIELDS, Biomes.SWAMP);
        this.tag(ModTags.Biomes.PURIFIED_WATER_BIOMES);
    }
}
