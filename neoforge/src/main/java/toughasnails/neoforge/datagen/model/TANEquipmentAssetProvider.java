/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.neoforge.datagen.model;

import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.EquipmentAsset;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModEquipmentAssets;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TANEquipmentAssetProvider implements DataProvider
{
    private final PackOutput.PathProvider pathProvider;

    public TANEquipmentAssetProvider(PackOutput output)
    {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    private static void bootstrap(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output)
    {
        output.accept(
                ModEquipmentAssets.LEAF,
                EquipmentClientInfo.builder()
                        .addHumanoidLayers(Identifier.fromNamespaceAndPath(ToughAsNails.MOD_ID, "leaf"), true)
                        .addHumanoidLayers(Identifier.fromNamespaceAndPath(ToughAsNails.MOD_ID, "leaf_overlay"), true)
                        .build()
        );
        output.accept(
                ModEquipmentAssets.WOOL,
                EquipmentClientInfo.builder()
                        .addHumanoidLayers(Identifier.fromNamespaceAndPath(ToughAsNails.MOD_ID, "wool"), true)
                        .addHumanoidLayers(Identifier.fromNamespaceAndPath(ToughAsNails.MOD_ID, "wool_overlay"), false)
                        .build()
        );
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> map = new HashMap<>();
        bootstrap((key, info) -> {
            if (map.putIfAbsent(key, info) != null) {
                throw new IllegalStateException("Tried to register equipment asset twice for id: " + key);
            }
        });
        return DataProvider.saveAll(output, EquipmentClientInfo.CODEC, this.pathProvider::json, map);
    }

    @Override
    public String getName() {
        return "Equipment Asset Definitions";
    }
}