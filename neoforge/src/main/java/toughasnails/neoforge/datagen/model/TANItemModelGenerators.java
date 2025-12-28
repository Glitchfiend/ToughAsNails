/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.neoforge.datagen.model;

import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.level.FoliageColor;
import toughasnails.api.item.TANItems;
import toughasnails.client.item.TemperatureProperty;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModEquipmentAssets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class TANItemModelGenerators extends ItemModelGenerators
{
    public static final Identifier TRIM_PREFIX_HELMET = prefixForSlotTrim("helmet");
    public static final Identifier TRIM_PREFIX_CHESTPLATE = prefixForSlotTrim("chestplate");
    public static final Identifier TRIM_PREFIX_LEGGINGS = prefixForSlotTrim("leggings");
    public static final Identifier TRIM_PREFIX_BOOTS = prefixForSlotTrim("boots");

    public final ItemModelOutput itemModelOutput;
    public final BiConsumer<Identifier, ModelInstance> modelOutput;

    public TANItemModelGenerators(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput)
    {
        super(itemModelOutput, modelOutput);
        this.itemModelOutput = itemModelOutput;
        this.modelOutput = modelOutput;
    }

    @Override
    public void run()
    {
        this.generateFlatItem(TANItems.ICE_CREAM, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.CHARC_0S, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.EMPTY_LEATHER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.LEATHER_DIRTY_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.LEATHER_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.LEATHER_PURIFIED_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.EMPTY_COPPER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.COPPER_DIRTY_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.COPPER_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.COPPER_PURIFIED_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.EMPTY_IRON_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.IRON_DIRTY_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.IRON_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.IRON_PURIFIED_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.EMPTY_GOLD_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.GOLD_DIRTY_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.GOLD_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.GOLD_PURIFIED_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.EMPTY_DIAMOND_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.DIAMOND_DIRTY_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.DIAMOND_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.DIAMOND_PURIFIED_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.EMPTY_NETHERITE_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.NETHERITE_DIRTY_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.NETHERITE_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.NETHERITE_PURIFIED_WATER_CANTEEN, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.DIRTY_WATER_BOTTLE, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.PURIFIED_WATER_BOTTLE, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.APPLE_JUICE, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.CACTUS_JUICE, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.CHORUS_FRUIT_JUICE, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.GLOW_BERRY_JUICE, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.MELON_JUICE, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.PUMPKIN_JUICE, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.SWEET_BERRY_JUICE, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(TANItems.TAN_ICON, ModelTemplates.FLAT_ITEM);

        this.generateTrimmableItemWithDefaultColor(TANItems.LEAF_HELMET, ModEquipmentAssets.LEAF, TRIM_PREFIX_HELMET, FoliageColor.FOLIAGE_DEFAULT);
        this.generateTrimmableItemWithDefaultColor(TANItems.LEAF_CHESTPLATE, ModEquipmentAssets.LEAF, TRIM_PREFIX_CHESTPLATE, FoliageColor.FOLIAGE_DEFAULT);
        this.generateTrimmableItemWithDefaultColor(TANItems.LEAF_LEGGINGS, ModEquipmentAssets.LEAF, TRIM_PREFIX_LEGGINGS, FoliageColor.FOLIAGE_DEFAULT);
        this.generateTrimmableItemWithDefaultColor(TANItems.LEAF_BOOTS, ModEquipmentAssets.LEAF, TRIM_PREFIX_BOOTS, FoliageColor.FOLIAGE_DEFAULT);
        this.generateTrimmableItem(TANItems.WOOL_HELMET, ModEquipmentAssets.WOOL, TRIM_PREFIX_HELMET, true);
        this.generateTrimmableItem(TANItems.WOOL_CHESTPLATE, ModEquipmentAssets.WOOL, TRIM_PREFIX_CHESTPLATE, true);
        this.generateTrimmableItem(TANItems.WOOL_LEGGINGS, ModEquipmentAssets.WOOL, TRIM_PREFIX_LEGGINGS, true);
        this.generateTrimmableItem(TANItems.WOOL_BOOTS, ModEquipmentAssets.WOOL, TRIM_PREFIX_BOOTS, true);

        this.generateThermometerItem(TANItems.THERMOMETER);
    }

    public void generateTrimmableItemWithDefaultColor(Item item, ResourceKey<EquipmentAsset> key, Identifier prefix, int defaultColor)
    {
        Identifier modelLocation = ModelLocationUtils.getModelLocation(item);
        Identifier textureLocation = TextureMapping.getItemTexture(item);
        Identifier overlayTextureLocation = TextureMapping.getItemTexture(item, "_overlay");
        List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> list = new ArrayList<>(TRIM_MATERIAL_MODELS.size());

        for (ItemModelGenerators.TrimMaterialData itemmodelgenerators$trimmaterialdata : TRIM_MATERIAL_MODELS) {
            Identifier Identifier3 = modelLocation.withSuffix("_" + itemmodelgenerators$trimmaterialdata.assets().base().suffix() + "_trim");
            Identifier Identifier4 = prefix.withSuffix("_" + itemmodelgenerators$trimmaterialdata.assets().assetId(key).suffix());
            ItemModel.Unbaked itemmodel$unbaked;
            this.generateLayeredItem(Identifier3, textureLocation, overlayTextureLocation, Identifier4);
            itemmodel$unbaked = ItemModelUtils.tintedModel(Identifier3, new Dye(defaultColor));

            list.add(ItemModelUtils.when(itemmodelgenerators$trimmaterialdata.materialKey(), itemmodel$unbaked));
        }

        ItemModel.Unbaked itemmodel$unbaked1;
        ModelTemplates.TWO_LAYERED_ITEM.create(modelLocation, TextureMapping.layered(textureLocation, overlayTextureLocation), this.modelOutput);
        itemmodel$unbaked1 = ItemModelUtils.tintedModel(modelLocation, new Dye(defaultColor));

        this.itemModelOutput.accept(item, ItemModelUtils.select(new TrimMaterialProperty(), itemmodel$unbaked1, list));
    }

    private static final int NUM_THERMOMETER_MODELS = 21;

    public void generateThermometerItem(Item item)
    {
        List<RangeSelectItemModel.Entry> entries = new ArrayList<>();

        for (int i = 0; i < NUM_THERMOMETER_MODELS; i++)
        {
            entries.add(ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(item, String.format("_%02d", i), ModelTemplates.FLAT_ITEM)), (float)i / (float)(NUM_THERMOMETER_MODELS - 1)));
        }

        this.itemModelOutput
                .accept(
                        item,
                        ItemModelUtils.rangeSelect(new TemperatureProperty(), 1.0F, entries)
                );
    }

    public static Identifier prefixForSlotTrim(String p_399619_) {
        return Identifier.withDefaultNamespace("trims/items/" + p_399619_ + "_trim");
    }
}
