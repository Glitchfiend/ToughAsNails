/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import toughasnails.api.block.TANBlocks;
import toughasnails.api.village.TANPoiTypes;
import toughasnails.api.village.TANVillagerProfessions;
import toughasnails.core.ToughAsNails;

import static toughasnails.core.ToughAsNails.MOD_ID;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class ModVillages
{
    public static void addBuildings(RegistryAccess registryAccess)
    {
        Registry<StructureTemplatePool> templatePools = registryAccess.lookupOrThrow(Registries.TEMPLATE_POOL);
        Registry<StructureProcessorList> processorLists = registryAccess.lookupOrThrow(Registries.PROCESSOR_LIST);

        addBuildingToPool(templatePools, processorLists, Identifier.parse("minecraft:village/desert/houses"), ToughAsNails.MOD_ID + ":village/desert/houses/desert_climatologist_1", 1);
        addBuildingToPool(templatePools, processorLists, Identifier.parse("minecraft:village/savanna/houses"), ToughAsNails.MOD_ID + ":village/savanna/houses/savanna_climatologist_1", 2);
        addBuildingToPool(templatePools, processorLists, Identifier.parse("minecraft:village/plains/houses"), ToughAsNails.MOD_ID + ":village/plains/houses/plains_climatologist_1", 2);
        addBuildingToPool(templatePools, processorLists, Identifier.parse("minecraft:village/taiga/houses"), ToughAsNails.MOD_ID + ":village/taiga/houses/taiga_climatologist_1", 3);
        addBuildingToPool(templatePools, processorLists, Identifier.parse("minecraft:village/snowy/houses"), ToughAsNails.MOD_ID + ":village/snowy/houses/snowy_climatologist_1", 2);
    }

    public static void registerPointsOfInterest(BiConsumer<Identifier, PoiType> func)
    {
        register(func, TANPoiTypes.CLIMATOLOGIST, getBlockStates(TANBlocks.THERMOREGULATOR), 1, 1);
    }

    public static void registerProfessions(BiConsumer<Identifier, VillagerProfession> func)
    {
        var tradeMap = new Int2ObjectOpenHashMap<ResourceKey<TradeSet>>();
        for (int level = 1; level <= 5; level++) {
            tradeMap.put(level, ResourceKey.create(Registries.TRADE_SET, Identifier.fromNamespaceAndPath(MOD_ID, "climatologist/level_" + level)));
        }
        register(func, TANVillagerProfessions.CLIMATOLOGIST, TANPoiTypes.CLIMATOLOGIST, SoundEvents.VILLAGER_WORK_ARMORER, tradeMap);
    }

    public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, Identifier poolRL, String nbtPieceRL, int weight)
    {
        templatePoolRegistry.get(poolRL).ifPresent(pool -> {
            Identifier emptyProcessor = Identifier.fromNamespaceAndPath("minecraft", "empty");
            Holder<StructureProcessorList> processorHolder = processorListRegistry.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, emptyProcessor));

            SinglePoolElement piece = SinglePoolElement.single(nbtPieceRL, processorHolder).apply(StructureTemplatePool.Projection.RIGID);

            for (int i = 0; i < weight; i++) {
                pool.value().templates.add(piece);
            }

            List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.value().rawTemplates);
            listOfPieceEntries.add(new Pair<>(piece, weight));
            pool.value().rawTemplates = listOfPieceEntries;
        });
    }

    private static PoiType register(BiConsumer<Identifier, PoiType> func, ResourceKey<PoiType> key, Set<BlockState> states, int maxTickets, int validRange)
    {
        PoiType type = new PoiType(states, maxTickets, validRange);
        func.accept(key.identifier(), type);
        registerBlockStates(key, states);
        return type;
    }

    private static VillagerProfession register(BiConsumer<Identifier, VillagerProfession> func, ResourceKey<VillagerProfession> key, ResourceKey<PoiType> poi, @Nullable SoundEvent workSound)
    {
        return register(func, key, poi, workSound, new Int2ObjectOpenHashMap<>());
    }

    private static VillagerProfession register(BiConsumer<Identifier, VillagerProfession> func, ResourceKey<VillagerProfession> key, ResourceKey<PoiType> poi, @Nullable SoundEvent workSound, Int2ObjectOpenHashMap<ResourceKey<TradeSet>> tradeMap)
    {
        return register(func, key, (h) -> h.is(poi), (h) -> h.is(poi), ImmutableSet.of(), ImmutableSet.of(), workSound, tradeMap);
    }

    private static VillagerProfession register(BiConsumer<Identifier, VillagerProfession> func, ResourceKey<VillagerProfession> key, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, @Nullable SoundEvent workSound)
    {
        return register(func, key, heldJobSite, acquirableJobSite, ImmutableSet.of(), ImmutableSet.of(), workSound, new Int2ObjectOpenHashMap<>());
    }

    private static VillagerProfession register(BiConsumer<Identifier, VillagerProfession> func, ResourceKey<VillagerProfession> key, ResourceKey<PoiType> poi, ImmutableSet<Item> requestedItems, ImmutableSet<Block> secondaryPoi, @Nullable SoundEvent workSound) {
        return register(func, key, ($$1x) -> $$1x.is(poi), ($$1x) -> $$1x.is(poi), requestedItems, secondaryPoi, workSound, new Int2ObjectOpenHashMap<>());
    }

    private static VillagerProfession register(BiConsumer<Identifier, VillagerProfession> func, ResourceKey<VillagerProfession> key, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, ImmutableSet<Item> requestedItems, ImmutableSet<Block> secondaryPoi, @Nullable SoundEvent workSound, Int2ObjectOpenHashMap<ResourceKey<TradeSet>> tradeMap) {
        return register(func, key, new VillagerProfession(Component.translatable("entity." + ToughAsNails.MOD_ID + ".villager." + key.identifier().getPath()), heldJobSite, acquirableJobSite, requestedItems, secondaryPoi, workSound, tradeMap));
    }

    private static VillagerProfession register(BiConsumer<Identifier, VillagerProfession> func, ResourceKey<VillagerProfession> key, VillagerProfession profession)
    {
        func.accept(key.identifier(), profession);
        return profession;
    }

    private static Set<BlockState> getBlockStates(Block block)
    {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }

    private static void registerBlockStates(ResourceKey<PoiType> key, Set<BlockState> states)
    {
        var holder = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getOrThrow(key);
        states.forEach(state -> {
            if (!PoiTypes.hasPoi(state)) PoiTypes.registerBlockStates(holder, Set.of(state));
        });
    }
}
