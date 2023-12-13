/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import toughasnails.api.TANAPI;
import toughasnails.api.enchantment.TANEnchantments;
import toughasnails.enchantment.ThermalTuningEnchantment;

import java.util.function.BiConsumer;

public class ModEnchantments
{
    public static void registerEnchantments(BiConsumer<ResourceLocation, Enchantment> func)
    {
        TANEnchantments.THERMAL_TUNING = register(func, "thermal_tuning", new ThermalTuningEnchantment());
    }

    private static Enchantment register(BiConsumer<ResourceLocation, Enchantment> func, String name, Enchantment enchantment)
    {
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), enchantment);
        return enchantment;
    }
}
