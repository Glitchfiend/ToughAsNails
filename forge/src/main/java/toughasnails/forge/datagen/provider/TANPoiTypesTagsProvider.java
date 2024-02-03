/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import toughasnails.api.village.TANPoiTypes;
import toughasnails.core.ToughAsNails;

import java.util.concurrent.CompletableFuture;

public class TANPoiTypesTagsProvider extends PoiTypeTagsProvider
{
    public TANPoiTypesTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(packOutput, lookupProvider, ToughAsNails.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(TANPoiTypes.CLIMATOLOGIST);
    }
}
