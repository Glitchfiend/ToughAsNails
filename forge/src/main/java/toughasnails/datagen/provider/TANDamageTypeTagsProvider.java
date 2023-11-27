/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import toughasnails.api.damagesource.TANDamageTypes;
import toughasnails.core.ToughAsNails;

import java.util.concurrent.CompletableFuture;

public class TANDamageTypeTagsProvider extends TagsProvider<DamageType>
{
    public TANDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, ToughAsNails.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(TANDamageTypes.HYPERTHERMIA);
    }
}