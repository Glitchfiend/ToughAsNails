/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import glitchcore.event.village.WandererTradesEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import toughasnails.api.item.TANItems;

import java.util.Arrays;

public class ModVillagerTrades
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
}
