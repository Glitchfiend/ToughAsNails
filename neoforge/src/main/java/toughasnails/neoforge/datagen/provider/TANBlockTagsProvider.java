/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.neoforge.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import toughasnails.api.block.TANBlocks;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModTags;

import java.util.concurrent.CompletableFuture;

public class TANBlockTagsProvider extends BlockTagsProvider
{
    public TANBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, lookupProvider, ToughAsNails.MOD_ID);
    }

    @SafeVarargs
    private static ResourceKey<Block>[] keys(Block... blocks)
    {
        ResourceKey<Block>[] result = new ResourceKey[blocks.length];
        for (int i = 0; i < blocks.length; i++)
        {
            result[i] = blocks[i].builtInRegistryHolder().key();
        }
        return result;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        // Vanilla tags
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(keys(TANBlocks.RAIN_COLLECTOR, TANBlocks.WATER_PURIFIER));
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(keys(TANBlocks.THERMOREGULATOR, TANBlocks.TEMPERATURE_GAUGE));

        // TAN tags
        this.tag(ModTags.Blocks.COOLING_BLOCKS).add(keys(Blocks.SOUL_FIRE, Blocks.SOUL_CAMPFIRE, Blocks.SOUL_LANTERN, Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.POWDER_SNOW_CAULDRON));
        this.tag(ModTags.Blocks.HEATING_BLOCKS).add(keys(Blocks.FIRE, Blocks.CAMPFIRE, Blocks.LANTERN, Blocks.LAVA, Blocks.MAGMA_BLOCK, Blocks.LAVA_CAULDRON));
        this.tag(ModTags.Blocks.PASSABLE_BLOCKS).addTags(BlockTags.DOORS, BlockTags.TRAPDOORS).add(keys(Blocks.SCAFFOLDING));
    }
}
