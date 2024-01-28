/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import com.mojang.datafixers.util.Pair;
import glitchcore.event.village.WandererTradesEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import toughasnails.api.item.TANItems;
import toughasnails.core.ToughAsNails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModVillages
{
    //Cost, Amount, Trades Until Disabled, Villager XP
    private static final VillagerTrades.ItemListing[] WANDERING_TRADER_GENERIC = new VillagerTrades.ItemListing[]{
            new ItemsForEmeralds(TANItems.ICE_CREAM, 2, 1, 4, 1),
            new ItemsForEmeralds(TANItems.CHARC_0S, 2, 1, 4, 1),
            new ItemsForEmeralds(TANItems.GLOW_BERRY_JUICE, 2, 1, 4, 1),
            new ItemsForEmeralds(TANItems.SWEET_BERRY_JUICE, 2, 1, 4, 1),
            new ItemsForEmeralds(TANItems.PUMPKIN_JUICE, 2, 1, 3, 1),
            new ItemsForEmeralds(TANItems.MELON_JUICE, 3, 1, 3, 1),
            new ItemsForEmeralds(TANItems.APPLE_JUICE, 4, 1, 2, 1),
            new ItemsForEmeralds(TANItems.CACTUS_JUICE, 4, 1, 2, 1)};

    private static final VillagerTrades.ItemListing[] WANDERING_TRADER_RARE = new VillagerTrades.ItemListing[]{
            new ItemsForEmeralds(TANItems.CHORUS_FRUIT_JUICE, 5, 1, 1, 1),
            new ItemsForEmeralds(TANItems.PURIFIED_WATER_BOTTLE, 4, 1, 2, 1)};


    public static void addWanderingVillagerTrades(WandererTradesEvent event)
    {
        event.addGenericTrades(Arrays.stream(WANDERING_TRADER_GENERIC).toList());
        event.addRareTrades(Arrays.stream(WANDERING_TRADER_RARE).toList());
    }

    public static void addBuildings(RegistryAccess registryAccess)
    {
        Registry<StructureTemplatePool> templatePools = registryAccess.registry(Registries.TEMPLATE_POOL).get();
        Registry<StructureProcessorList> processorLists = registryAccess.registry(Registries.PROCESSOR_LIST).get();

        addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/desert/houses"), ToughAsNails.MOD_ID + ":village/desert/houses/desert_climatologist_1", 5);
        addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/plains/houses"), ToughAsNails.MOD_ID + ":village/plains/houses/plains_climatologist_1", 5);
        addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/savanna/houses"), ToughAsNails.MOD_ID + ":village/savanna/houses/savanna_climatologist_1", 5);
        addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/snowy/houses"), ToughAsNails.MOD_ID + ":village/snowy/houses/snowy_climatologist_1", 5);
        addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/taiga/houses"), ToughAsNails.MOD_ID + ":village/taiga/houses/taiga_climatologist_1", 5);
    }

    static class ItemsForEmeralds implements VillagerTrades.ItemListing {
        private final ItemStack itemStack;
        private final int emeraldCost;
        private final int numberOfItems;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public ItemsForEmeralds(Item p_35746_, int p_35747_, int p_35748_, int p_35749_, int p_35750_) {
            this(new ItemStack(p_35746_), p_35747_, p_35748_, p_35749_, p_35750_);
        }

        public ItemsForEmeralds(ItemStack p_35752_, int p_35753_, int p_35754_, int p_35755_, int p_35756_) {
            this(p_35752_, p_35753_, p_35754_, p_35755_, p_35756_, 0.05F);
        }

        public ItemsForEmeralds(ItemStack p_35758_, int p_35759_, int p_35760_, int p_35761_, int p_35762_, float p_35763_) {
            this.itemStack = p_35758_;
            this.emeraldCost = p_35759_;
            this.numberOfItems = p_35760_;
            this.maxUses = p_35761_;
            this.villagerXp = p_35762_;
            this.priceMultiplier = p_35763_;
        }

        public MerchantOffer getOffer(Entity p_219699_, RandomSource p_219700_) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight)
    {
        StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
        if (pool == null) return;

        ResourceLocation emptyProcessor = new ResourceLocation("minecraft", "empty");
        Holder<StructureProcessorList> processorHolder = processorListRegistry.getHolderOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, emptyProcessor));

        SinglePoolElement piece = SinglePoolElement.single(nbtPieceRL, processorHolder).apply(StructureTemplatePool.Projection.RIGID);

        for (int i = 0; i < weight; i++) {
            pool.templates.add(piece);
        }

        List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
        listOfPieceEntries.add(new Pair<>(piece, weight));
        pool.rawTemplates = listOfPieceEntries;
    }
}
