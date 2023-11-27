/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.RegistryObject;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.potion.TANPotions;
import toughasnails.core.ToughAsNails;
import toughasnails.potion.ThirstEffect;

import java.util.function.Supplier;

public class ModPotions
{
    public static void init()
    {
        registerEffects();
        registerPotions();
    }

    public static void registerEffects()
    {
        TANEffects.THIRST = registerEffect("thirst", () -> new ThirstEffect(MobEffectCategory.HARMFUL, 0x76DB4C));
        TANEffects.ICE_RESISTANCE = registerEffect("ice_resistance", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0x77A9FF));
        TANEffects.CLIMATE_CLEMENCY = registerEffect("climate_clemency", () -> new MobEffect(MobEffectCategory.NEUTRAL, 0xB6B6B6));
    }

    public static void registerPotions()
    {
        TANPotions.ICE_RESISTANCE = registerPotion("ice_resistance", () -> new Potion(new MobEffectInstance(TANEffects.ICE_RESISTANCE.get(), 1200)));
        TANPotions.LONG_ICE_RESISTANCE = registerPotion("long_ice_resistance", () -> new Potion(new MobEffectInstance(TANEffects.ICE_RESISTANCE.get(), 2400)));
    }

    public static RegistryObject<MobEffect> registerEffect(String name, Supplier<MobEffect> effect)
    {
        return ToughAsNails.MOB_EFFECT_REGISTER.register(name, effect);
    }

    public static RegistryObject<Potion> registerPotion(String name, Supplier<Potion> potion)
    {
        return ToughAsNails.POTION_REGISTER.register(name, potion);
    }
}
