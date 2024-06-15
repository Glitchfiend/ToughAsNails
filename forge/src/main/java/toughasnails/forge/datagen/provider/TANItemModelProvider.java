/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.datagen.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import toughasnails.core.ToughAsNails;

public class TANItemModelProvider extends ItemModelProvider
{
    private static final int NUM_THERMOMETER_MODELS = 21;

    public TANItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, ToughAsNails.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        this.registerThermometerModels();
    }

    private void registerThermometerModels()
    {
        var thermometer = getBuilder(loc("thermometer").toString()).parent(new ModelFile.UncheckedModelFile("item/generated"));
        ModelFile[] thermometerModels = new ModelFile[NUM_THERMOMETER_MODELS];

        // Populate standard and tropical arrays
        for (int i = 0; i < NUM_THERMOMETER_MODELS; i++)
        {
            String pathIndex = String.format("%02d", i);
            thermometerModels[i] = this.basicItem(loc("thermometer_" + pathIndex));
        }

        // Thermometer overrides
        for (int i = 0; i < NUM_THERMOMETER_MODELS; i++)
        {
            thermometer.override()
                .predicate(loc("temperature"), (float)i / (float)(NUM_THERMOMETER_MODELS - 1))
                .model(thermometerModels[i]);
        }
    }

    private static ResourceLocation loc(String name)
    {
        return ResourceLocation.fromNamespaceAndPath(ToughAsNails.MOD_ID, name);
    }
}
