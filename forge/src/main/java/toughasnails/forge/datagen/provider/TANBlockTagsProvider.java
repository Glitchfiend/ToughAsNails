/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import toughasnails.api.block.TANBlocks;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModTags;

import java.util.concurrent.CompletableFuture;

public class TANBlockTagsProvider extends BlockTagsProvider
{
    public TANBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, ToughAsNails.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        // Vanilla tags
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(TANBlocks.RAIN_COLLECTOR, TANBlocks.WATER_PURIFIER);
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(TANBlocks.THERMOREGULATOR, TANBlocks.TEMPERATURE_GAUGE);

        // TAN tags
        this.tag(ModTags.Blocks.COOLING_BLOCKS).add(Blocks.SOUL_FIRE, Blocks.SOUL_CAMPFIRE, Blocks.SOUL_LANTERN, Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.POWDER_SNOW_CAULDRON);
        this.tag(ModTags.Blocks.HEATING_BLOCKS).add(Blocks.FIRE, Blocks.CAMPFIRE, Blocks.LANTERN, Blocks.LAVA, Blocks.MAGMA_BLOCK, Blocks.LAVA_CAULDRON);
    }
}
