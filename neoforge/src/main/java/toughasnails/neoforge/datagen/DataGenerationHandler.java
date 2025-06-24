/************************************************************************even*******
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.neoforge.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModEnchantments;
import toughasnails.neoforge.datagen.loot.TANLootTableProvider;
import toughasnails.neoforge.datagen.model.TANEquipmentAssetProvider;
import toughasnails.neoforge.datagen.model.TANModelProvider;
import toughasnails.neoforge.datagen.provider.*;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ToughAsNails.MOD_ID)
public class DataGenerationHandler
{
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap)
        .add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event)
    {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        var datapackProvider = generator.addProvider(true, new DatapackBuiltinEntriesProvider(output, lookupProvider, BUILDER, Set.of(ToughAsNails.MOD_ID)));

        // Recipes
        generator.addProvider(true, new TANRecipeProvider.Runner(output, event.getLookupProvider()));

        // Loot
        generator.addProvider(true, TANLootTableProvider.create(output, event.getLookupProvider()));

        // Tags
        var blocksTagProvider = generator.addProvider(true, new TANBlockTagsProvider(output, datapackProvider.getRegistryProvider()));
        generator.addProvider(true, new TANItemTagsProvider(output, datapackProvider.getRegistryProvider()));
        generator.addProvider(true, new TANBiomeTagsProvider(output, datapackProvider.getRegistryProvider()));
        generator.addProvider(true, new TANDamageTypeTagsProvider(output, datapackProvider.getRegistryProvider()));
        generator.addProvider(true, new TANTrimMaterialTagsProvider(output, datapackProvider.getRegistryProvider()));
        generator.addProvider(true, new TANPoiTypesTagsProvider(output, datapackProvider.getRegistryProvider()));
        generator.addProvider(true, new TANEnchantmentTagsProvider(output, datapackProvider.getRegistryProvider()));

        // Client
        generator.addProvider(true, new TANEquipmentAssetProvider(output));
        generator.addProvider(true, new TANModelProvider(output));
    }
}
