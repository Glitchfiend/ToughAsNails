/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import toughasnails.api.enchantment.TANEnchantments;

public class ModEnchantments
{
    public static void bootstrap(BootstrapContext<Enchantment> context)
    {
        HolderGetter<Enchantment> enchantmentGetter = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> itemGetter = context.lookup(Registries.ITEM);
        register(
                context,
                TANEnchantments.THERMAL_TUNING,
                Enchantment.enchantment(
                    Enchantment.definition(itemGetter.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE), 2, 1, Enchantment.dynamicCost(25, 25), Enchantment.dynamicCost(75, 25), 4, EquipmentSlotGroup.CHEST)
                )
                .exclusiveWith(enchantmentGetter.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
        );

        register(
            context,
            TANEnchantments.WATER_CLEANSING,
            Enchantment.enchantment(
                Enchantment.definition(itemGetter.getOrThrow(ModTags.Items.CANTEEN), 2, 1, Enchantment.dynamicCost(25, 25), Enchantment.dynamicCost(75, 25), 4, EquipmentSlotGroup.MAINHAND)
            )
            .exclusiveWith(enchantmentGetter.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
        );
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder)
    {
        context.register(key, builder.build(key.location()));
    }
}
