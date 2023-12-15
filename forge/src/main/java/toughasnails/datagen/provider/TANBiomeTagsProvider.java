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
        this.tag(ModTags.Biomes.ICY_BIOMES);
        this.tag(ModTags.Biomes.COLD_BIOMES);
        this.tag(ModTags.Biomes.NEUTRAL_BIOMES);
        this.tag(ModTags.Biomes.WARM_BIOMES);
        this.tag(ModTags.Biomes.HOT_BIOMES);

        // Water purity tags
        this.tag(ModTags.Biomes.DIRTY_WATER_BIOMES).add(Biomes.MANGROVE_SWAMP, Biomes.MUSHROOM_FIELDS, Biomes.SWAMP);
        this.tag(ModTags.Biomes.PURIFIED_WATER_BIOMES);
    }
}
