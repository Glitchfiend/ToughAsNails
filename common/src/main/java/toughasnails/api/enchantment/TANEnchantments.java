/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.api.enchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import toughasnails.core.ToughAsNails;

public class TANEnchantments
{
    public static ResourceKey<Enchantment> THERMAL_TUNING = key("thermal_tuning");
    public static ResourceKey<Enchantment> WATER_CLEANSING = key("water_cleansing");

    private static ResourceKey<Enchantment> key(String name)
    {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ToughAsNails.MOD_ID, name));
    }
}
