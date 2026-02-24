/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.neoforge.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import toughasnails.api.item.TANItems;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModTags;

import java.util.concurrent.CompletableFuture;

public class TANEntityTypeTagsProvider extends EntityTypeTagsProvider
{
    public TANEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, lookupProvider, ToughAsNails.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        // Dyeable
        this.tag(ModTags.EntityTypes.NEUTRALISING_MOUNTS)
                .addTag(Tags.EntityTypes.MINECARTS)
                .addTag(Tags.EntityTypes.BOATS)
                .addOptionalTag(TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("mts", "builder_seat")));
    }
}
