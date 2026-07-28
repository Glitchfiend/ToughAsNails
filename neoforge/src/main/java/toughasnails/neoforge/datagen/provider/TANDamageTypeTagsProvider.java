/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.neoforge.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import toughasnails.api.damagesource.TANDamageTypes;

import java.util.concurrent.CompletableFuture;

public class TANDamageTypeTagsProvider extends TagsProvider<DamageType>
{
    public TANDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, Registries.DAMAGE_TYPE, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(TANDamageTypes.HYPERTHERMIA, TANDamageTypes.THIRST);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(TANDamageTypes.THIRST);
    }
}