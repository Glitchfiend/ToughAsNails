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
import toughasnails.init.ModItems;
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
        this.tag(ModTags.Items.COOLING_ARMOR).add(TANItems.LEAF_HELMET.get(), TANItems.LEAF_CHESTPLATE.get(), TANItems.LEAF_LEGGINGS.get(), TANItems.LEAF_BOOTS.get());
        this.tag(ModTags.Items.HEATING_ARMOR).add(TANItems.WOOL_HELMET.get(), TANItems.WOOL_CHESTPLATE.get(), TANItems.WOOL_LEGGINGS.get(), TANItems.WOOL_BOOTS.get());

        // Temperature items
        this.tag(ModTags.Items.COOLING_ITEMS).add(Items.POWDER_SNOW_BUCKET);
        this.tag(ModTags.Items.HEATING_ITEMS).add(Items.LAVA_BUCKET);

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
        this.tag(ModTags.Items.ONE_THIRST_DRINKS).add(TANItems.DIRTY_WATER_BOTTLE.get(), TANItems.DIRTY_WATER_CANTEEN.get());
        this.tag(ModTags.Items.TWO_THIRST_DRINKS).add(Items.MILK_BUCKET);
        this.tag(ModTags.Items.THREE_THIRST_DRINKS).add(Items.POTION, TANItems.WATER_CANTEEN.get());
        this.tag(ModTags.Items.FOUR_THIRST_DRINKS);
        this.tag(ModTags.Items.FIVE_THIRST_DRINKS).add(TANItems.PURIFIED_WATER_BOTTLE.get(), TANItems.PURIFIED_WATER_CANTEEN.get());
        this.tag(ModTags.Items.SIX_THIRST_DRINKS).add(TANItems.GLOW_BERRY_JUICE.get(), TANItems.PUMPKIN_JUICE.get(), TANItems.SWEET_BERRY_JUICE.get());
        this.tag(ModTags.Items.SEVEN_THIRST_DRINKS).add(TANItems.CACTUS_JUICE.get(), TANItems.MELON_JUICE.get());
        this.tag(ModTags.Items.EIGHT_THIRST_DRINKS).add(TANItems.APPLE_JUICE.get());
        this.tag(ModTags.Items.NINE_THIRST_DRINKS);
        this.tag(ModTags.Items.TEN_THIRST_DRINKS).add(TANItems.CHORUS_FRUIT_JUICE.get());
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
        this.tag(ModTags.Items.TWENTY_FIVE_POISON_CHANCE_DRINKS).add(Items.POTION, TANItems.WATER_CANTEEN.get());
        this.tag(ModTags.Items.FIFTY_POISON_CHANCE_DRINKS);
        this.tag(ModTags.Items.SEVENTY_FIVE_POISON_CHANCE_DRINKS).add(TANItems.DIRTY_WATER_BOTTLE.get(), TANItems.DIRTY_WATER_CANTEEN.get());
        this.tag(ModTags.Items.ONE_HUNDRED_POISON_CHANCE_DRINKS);

        // Hydration
        this.tag(ModTags.Items.TEN_HYDRATION_DRINKS).add(TANItems.DIRTY_WATER_BOTTLE.get(), TANItems.DIRTY_WATER_CANTEEN.get(), TANItems.GLOW_BERRY_JUICE.get(), TANItems.SWEET_BERRY_JUICE.get());
        this.tag(ModTags.Items.TWENTY_HYDRATION_DRINKS).add(Items.POTION, TANItems.WATER_CANTEEN.get(), TANItems.CHORUS_FRUIT_JUICE.get());
        this.tag(ModTags.Items.THIRTY_HYDRATION_DRINKS).add(Items.MILK_BUCKET, TANItems.PUMPKIN_JUICE.get());
        this.tag(ModTags.Items.FOURTY_HYDRATION_DRINKS);
        this.tag(ModTags.Items.FIFTY_HYDRATION_DRINKS).add(TANItems.PURIFIED_WATER_BOTTLE.get(), TANItems.PURIFIED_WATER_CANTEEN.get());
        this.tag(ModTags.Items.SIXTY_HYDRATION_DRINKS).add(TANItems.APPLE_JUICE.get(), TANItems.MELON_JUICE.get());
        this.tag(ModTags.Items.SEVENTY_HYDRATION_DRINKS);
        this.tag(ModTags.Items.EIGHTY_HYDRATION_DRINKS).add(TANItems.CACTUS_JUICE.get());
        this.tag(ModTags.Items.NINETY_HYDRATION_DRINKS);
        this.tag(ModTags.Items.ONE_HUNDRED_HYDRATION_DRINKS);
    }
}
