/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import toughasnails.api.item.TANItems;
import toughasnails.core.ToughAsNails;
import toughasnails.core.ToughAsNailsForge;
import toughasnails.init.ModTags;

import java.util.concurrent.CompletableFuture;

public class TANItemTagsProvider extends ItemTagsProvider
{
    public TANItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockLookup, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, blockLookup, ToughAsNails.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        // Temperature armor
        this.tag(ModTags.Items.COOLING_ARMOR).add(TANItems.LEAF_HELMET, TANItems.LEAF_CHESTPLATE, TANItems.LEAF_LEGGINGS, TANItems.LEAF_BOOTS);
        this.tag(ModTags.Items.HEATING_ARMOR).add(TANItems.WOOL_HELMET, TANItems.WOOL_CHESTPLATE, TANItems.WOOL_LEGGINGS, TANItems.WOOL_BOOTS);

        // Temperature items
        this.tag(ModTags.Items.COOLING_HELD_ITEMS).add(Items.POWDER_SNOW_BUCKET);
        this.tag(ModTags.Items.HEATING_HELD_ITEMS).add(Items.LAVA_BUCKET);

        // Drinks
        this.tag(ModTags.Items.DRINKS).addTags(
            ModTags.Items.ONE_THIRST_DRINKS,
            ModTags.Items.TWO_THIRST_DRINKS,
            ModTags.Items.THREE_THIRST_DRINKS,
            ModTags.Items.FOUR_THIRST_DRINKS,
            ModTags.Items.FIVE_THIRST_DRINKS,
            ModTags.Items.SIX_THIRST_DRINKS,
            ModTags.Items.SEVEN_THIRST_DRINKS,
            ModTags.Items.EIGHT_THIRST_DRINKS,
            ModTags.Items.NINE_THIRST_DRINKS,
            ModTags.Items.TEN_THIRST_DRINKS,
            ModTags.Items.ELEVEN_THIRST_DRINKS,
            ModTags.Items.TWELVE_THIRST_DRINKS,
            ModTags.Items.THIRTEEN_THIRST_DRINKS,
            ModTags.Items.FOURTEEN_THIRST_DRINKS,
            ModTags.Items.FIFTEEN_THIRST_DRINKS,
            ModTags.Items.SIXTEEN_THIRST_DRINKS,
            ModTags.Items.SEVENTEEN_THIRST_DRINKS,
            ModTags.Items.EIGHTEEN_THIRST_DRINKS,
            ModTags.Items.NINETEEN_THIRST_DRINKS,
            ModTags.Items.TWENTY_THIRST_DRINKS
        );
        this.tag(ModTags.Items.ONE_THIRST_DRINKS).add(TANItems.DIRTY_WATER_BOTTLE, TANItems.DIRTY_WATER_CANTEEN);
        this.tag(ModTags.Items.TWO_THIRST_DRINKS).add(Items.MILK_BUCKET);
        this.tag(ModTags.Items.THREE_THIRST_DRINKS).add(Items.POTION, TANItems.WATER_CANTEEN);
        this.tag(ModTags.Items.FOUR_THIRST_DRINKS);
        this.tag(ModTags.Items.FIVE_THIRST_DRINKS).add(TANItems.PURIFIED_WATER_BOTTLE, TANItems.PURIFIED_WATER_CANTEEN);
        this.tag(ModTags.Items.SIX_THIRST_DRINKS).add(TANItems.GLOW_BERRY_JUICE, TANItems.PUMPKIN_JUICE, TANItems.SWEET_BERRY_JUICE);
        this.tag(ModTags.Items.SEVEN_THIRST_DRINKS).add(TANItems.CACTUS_JUICE, TANItems.MELON_JUICE);
        this.tag(ModTags.Items.EIGHT_THIRST_DRINKS).add(TANItems.APPLE_JUICE);
        this.tag(ModTags.Items.NINE_THIRST_DRINKS);
        this.tag(ModTags.Items.TEN_THIRST_DRINKS).add(TANItems.CHORUS_FRUIT_JUICE);
        this.tag(ModTags.Items.ELEVEN_THIRST_DRINKS);
        this.tag(ModTags.Items.TWELVE_THIRST_DRINKS);
        this.tag(ModTags.Items.THIRTEEN_THIRST_DRINKS);
        this.tag(ModTags.Items.FOURTEEN_THIRST_DRINKS);
        this.tag(ModTags.Items.FIFTEEN_THIRST_DRINKS);
        this.tag(ModTags.Items.SIXTEEN_THIRST_DRINKS);
        this.tag(ModTags.Items.SEVENTEEN_THIRST_DRINKS);
        this.tag(ModTags.Items.EIGHTEEN_THIRST_DRINKS);
        this.tag(ModTags.Items.NINETEEN_THIRST_DRINKS);
        this.tag(ModTags.Items.TWENTY_THIRST_DRINKS);

        // Poison chance
        this.tag(ModTags.Items.TWENTY_FIVE_POISON_CHANCE_DRINKS).add(Items.POTION, TANItems.WATER_CANTEEN);
        this.tag(ModTags.Items.FIFTY_POISON_CHANCE_DRINKS);
        this.tag(ModTags.Items.SEVENTY_FIVE_POISON_CHANCE_DRINKS).add(TANItems.DIRTY_WATER_BOTTLE, TANItems.DIRTY_WATER_CANTEEN);
        this.tag(ModTags.Items.ONE_HUNDRED_POISON_CHANCE_DRINKS);

        // Hydration
        this.tag(ModTags.Items.TEN_HYDRATION_DRINKS).add(TANItems.DIRTY_WATER_BOTTLE, TANItems.DIRTY_WATER_CANTEEN, TANItems.GLOW_BERRY_JUICE, TANItems.SWEET_BERRY_JUICE);
        this.tag(ModTags.Items.TWENTY_HYDRATION_DRINKS).add(Items.POTION, TANItems.WATER_CANTEEN, TANItems.CHORUS_FRUIT_JUICE);
        this.tag(ModTags.Items.THIRTY_HYDRATION_DRINKS).add(Items.MILK_BUCKET, TANItems.PUMPKIN_JUICE);
        this.tag(ModTags.Items.FOURTY_HYDRATION_DRINKS);
        this.tag(ModTags.Items.FIFTY_HYDRATION_DRINKS).add(TANItems.PURIFIED_WATER_BOTTLE, TANItems.PURIFIED_WATER_CANTEEN);
        this.tag(ModTags.Items.SIXTY_HYDRATION_DRINKS).add(TANItems.APPLE_JUICE, TANItems.MELON_JUICE);
        this.tag(ModTags.Items.SEVENTY_HYDRATION_DRINKS);
        this.tag(ModTags.Items.EIGHTY_HYDRATION_DRINKS).add(TANItems.CACTUS_JUICE);
        this.tag(ModTags.Items.NINETY_HYDRATION_DRINKS);
        this.tag(ModTags.Items.ONE_HUNDRED_HYDRATION_DRINKS);
    }
}
