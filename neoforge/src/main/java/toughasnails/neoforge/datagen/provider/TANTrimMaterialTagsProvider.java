/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.neoforge.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.jetbrains.annotations.Nullable;
import toughasnails.init.ModTags;

import java.util.concurrent.CompletableFuture;

public class TANTrimMaterialTagsProvider extends TagsProvider<TrimMaterial>
{
    public TANTrimMaterialTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, Registries.TRIM_MATERIAL, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        this.tag(ModTags.Trims.COOLING_TRIMS);
        this.tag(ModTags.Trims.HEATING_TRIMS);
    }
}
