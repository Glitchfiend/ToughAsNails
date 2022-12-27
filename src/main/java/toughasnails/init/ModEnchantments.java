/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import toughasnails.api.enchantment.TANEnchantments;
import toughasnails.core.ToughAsNails;
import toughasnails.enchantment.ThermalTuningEnchantment;

import java.util.function.Supplier;

public class ModEnchantments
{
    public static void init()
    {
        registerEnchantments();
    }

    private static void registerEnchantments()
    {
        TANEnchantments.THERMAL_TUNING = register("thermal_tuning", ThermalTuningEnchantment::new);
    }

    public static RegistryObject<Enchantment> register(String name, Supplier<Enchantment> enchantment)
    {
        return ToughAsNails.ENCHANTMENT_REGISTER.register(name, enchantment);
    }
}
