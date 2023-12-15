/************************************************************************even*******
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.datagen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import toughasnails.core.ToughAsNailsForge;
import toughasnails.datagen.loot.TANLootTableProvider;
import toughasnails.datagen.provider.*;

import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ToughAsNailsForge.MOD_ID)
public class DataGenerationHandler
{
    private static final RegistrySetBuilder REG_BUILDER = new RegistrySetBuilder()
        .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput output = generator.getPackOutput();

        var datapackProvider = generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), REG_BUILDER, Set.of(ToughAsNailsForge.MOD_ID)));

        // Recipes
        generator.addProvider(event.includeServer(), new TANRecipeProvider(output));

        // Loot
        generator.addProvider(event.includeServer(), TANLootTableProvider.create(output));

        // Tags
        var blocksTagProvider = generator.addProvider(event.includeServer(), new TANBlockTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANItemTagsProvider(output, datapackProvider.getRegistryProvider(), blocksTagProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANBiomeTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new TANDamageTypeTagsProvider(output, datapackProvider.getRegistryProvider(), existingFileHelper));
    }
}
