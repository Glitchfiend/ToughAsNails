/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import glitchcore.event.village.WandererTradesEvent;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.material.MapColor;
import toughasnails.api.TANAPI;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.item.TANItems;
import toughasnails.api.village.TANPoiTypes;
import toughasnails.api.village.TANVillagerProfessions;
import toughasnails.block.RainCollectorBlock;
import toughasnails.block.TemperatureGaugeBlock;
import toughasnails.block.ThermoregulatorBlock;
import toughasnails.block.WaterPurifierBlock;
import toughasnails.core.ToughAsNails;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

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

        addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/desert/houses"), ToughAsNails.MOD_ID + ":village/desert/houses/desert_climatologist_1", 2);
        addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/plains/houses"), ToughAsNails.MOD_ID + ":village/plains/houses/plains_climatologist_1", 2);
        addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/savanna/houses"), ToughAsNails.MOD_ID + ":village/savanna/houses/savanna_climatologist_1", 2);
        addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/snowy/houses"), ToughAsNails.MOD_ID + ":village/snowy/houses/snowy_climatologist_1", 2);
        addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/taiga/houses"), ToughAsNails.MOD_ID + ":village/taiga/houses/taiga_climatologist_1", 2);
    }

    public static void registerPointsOfInterest(BiConsumer<ResourceLocation, PoiType> func)
    {
        register(func, TANPoiTypes.CLIMATOLOGIST, getBlockStates(TANBlocks.THERMOREGULATOR), 1, 1);
    }

    public static void registerProfessions(BiConsumer<ResourceLocation, VillagerProfession> func)
    {
        TANVillagerProfessions.CLIMATOLOGIST = register(func, "climatologist", TANPoiTypes.CLIMATOLOGIST, SoundEvents.VILLAGER_WORK_ARMORER);
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

    private static PoiType register(BiConsumer<ResourceLocation, PoiType> func, ResourceKey<PoiType> key, Set<BlockState> states, int maxTickets, int validRange)
    {
        PoiType type = new PoiType(states, maxTickets, validRange);
        func.accept(key.location(), type);
        registerBlockStates(key, states);
        return type;
    }

    private static VillagerProfession register(BiConsumer<ResourceLocation, VillagerProfession> func, String name, ResourceKey<PoiType> poi, @Nullable SoundEvent workSound)
    {
        return register(func, name, (h) -> h.is(poi), (h) -> h.is(poi), workSound);
    }

    private static VillagerProfession register(BiConsumer<ResourceLocation, VillagerProfession> func, String name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, @Nullable SoundEvent workSound)
    {
        return register(func, name, heldJobSite, acquirableJobSite, ImmutableSet.of(), ImmutableSet.of(), workSound);
    }

    private static VillagerProfession register(BiConsumer<ResourceLocation, VillagerProfession> func, String name, ResourceKey<PoiType> poi, ImmutableSet<Item> requestedItems, ImmutableSet<Block> secondaryPoi, @Nullable SoundEvent workSound) {
        return register(func, name, ($$1x) -> $$1x.is(poi), ($$1x) -> $$1x.is(poi), requestedItems, secondaryPoi, workSound);
    }

    private static VillagerProfession register(BiConsumer<ResourceLocation, VillagerProfession> func, String name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, ImmutableSet<Item> requestedItems, ImmutableSet<Block> secondaryPoi, @Nullable SoundEvent workSound) {
        return register(func, name, new VillagerProfession(ToughAsNails.MOD_ID + ":" + name, heldJobSite, acquirableJobSite, requestedItems, secondaryPoi, workSound));
    }

    private static VillagerProfession register(BiConsumer<ResourceLocation, VillagerProfession> func, String name, VillagerProfession profession)
    {
        func.accept(new ResourceLocation(ToughAsNails.MOD_ID, name), profession);
        return profession;
    }

    private static Set<BlockState> getBlockStates(Block block)
    {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }

    private static void registerBlockStates(ResourceKey<PoiType> key, Set<BlockState> states)
    {
        var holder = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(key);
        states.forEach(state -> {
            if (!PoiTypes.hasPoi(state)) PoiTypes.registerBlockStates(holder, Set.of(state));
        });
    }
}
