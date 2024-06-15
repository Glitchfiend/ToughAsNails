/************************************************************************even*******
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.datagen;

import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import toughasnails.api.TANAPI;
import toughasnails.core.ToughAsNails;
import toughasnails.forge.datagen.loot.TANLootTableProvider;
import toughasnails.forge.datagen.provider.*;
import toughasnails.init.ModEnchantments;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ToughAsNails.MOD_ID)
public class DataGenerationHandler
{
    private static final RegistrySetBuilder REG_BUILDER = new RegistrySetBuilder()
        .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap)
        .add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput output = generator.getPackOutput();

        var datapackProvider = generator.addProvider(event.includeServer(), new RegistriesDatapackGenerator(output, event.getLookupProvider().thenApply(r -> constructRegistries(r, REG_BUILDER)), Set.of(TANAPI.MOD_ID)));

        // Recipes
        generator.addProvider(event.includeServer(), new TANRecipeProvider(output, event.getLookupProvider()));

        // Loot
        generator.addProvider(event.includeServer(), TANLootTableProvider.create(output, event.getLookupProvider()));

        // Tags
        var blocksTagProvider = generator.addProvider(event.includeServer(), new TANBlockTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANItemTagsProvider(output, datapackProvider.getRegistryProvider(), blocksTagProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANBiomeTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANDamageTypeTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANTrimMaterialTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANPoiTypesTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANEnchantmentTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));

        // Client
        generator.addProvider(event.includeClient(), new TANItemModelProvider(output, existingFileHelper));
    }


    private static HolderLookup.Provider constructRegistries(HolderLookup.Provider original, RegistrySetBuilder datapackEntriesBuilder)
    {
        Cloner.Factory clonerFactory = new Cloner.Factory();
        var builderKeys = new HashSet<>(datapackEntriesBuilder.getEntryKeys());
        RegistryDataLoader.WORLDGEN_REGISTRIES.stream().forEach(data -> {
            // Add keys for missing registries
            if (!builderKeys.contains(data.key()))
                datapackEntriesBuilder.add(data.key(), context -> {});

            data.runWithArguments(clonerFactory::addCodec);
        });

        return datapackEntriesBuilder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original, clonerFactory).patches();
    }
}
