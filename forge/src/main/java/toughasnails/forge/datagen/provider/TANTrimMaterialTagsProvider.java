/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModTags;

import java.util.concurrent.CompletableFuture;

public class TANTrimMaterialTagsProvider extends TagsProvider<TrimMaterial>
{
    public TANTrimMaterialTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, Registries.TRIM_MATERIAL, lookupProvider, ToughAsNails.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        this.tag(ModTags.Trims.COOLING_TRIMS);
        this.tag(ModTags.Trims.HEATING_TRIMS);
    }
}
