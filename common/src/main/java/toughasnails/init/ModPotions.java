/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import toughasnails.api.TANAPI;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.potion.TANPotions;
import toughasnails.potion.ThirstEffect;

import java.util.function.BiConsumer;

public class ModPotions
{
    public static void registerEffects(BiConsumer<ResourceLocation, MobEffect> func)
    {
        TANEffects.THIRST = registerEffect(func, "thirst", new ThirstEffect(MobEffectCategory.HARMFUL, 0x76DB4C));
        TANEffects.ICE_RESISTANCE = registerEffect(func, "ice_resistance", new MobEffect(MobEffectCategory.BENEFICIAL, 0x77A9FF));
        TANEffects.CLIMATE_CLEMENCY = registerEffect(func, "climate_clemency", new MobEffect(MobEffectCategory.BENEFICIAL, 0xB6B6B6));
        TANEffects.INTERNAL_WARMTH = registerEffect(func, "internal_warmth", new MobEffect(MobEffectCategory.BENEFICIAL, 0xFFFAD9));
        TANEffects.INTERNAL_CHILL = registerEffect(func, "internal_chill", new MobEffect(MobEffectCategory.BENEFICIAL, 0xE1FCFF));
    }

    public static void registerPotions(BiConsumer<ResourceLocation, Potion> func)
    {
        TANPotions.ICE_RESISTANCE = registerPotion(func, "ice_resistance", new Potion(new MobEffectInstance(TANEffects.ICE_RESISTANCE, 3600)));
        TANPotions.LONG_ICE_RESISTANCE = registerPotion(func, "long_ice_resistance", new Potion(new MobEffectInstance(TANEffects.ICE_RESISTANCE, 9600)));

        // Register potion recipes after potions are registered
        registerPotionRecipes();
    }

    public static void registerPotionRecipes()
    {
        PotionBrewing.addMix(Potions.AWKWARD, Items.SNOWBALL, TANPotions.ICE_RESISTANCE);
        PotionBrewing.addMix(TANPotions.ICE_RESISTANCE, Items.REDSTONE, TANPotions.LONG_ICE_RESISTANCE);
    }

    private static MobEffect registerEffect(BiConsumer<ResourceLocation, MobEffect> func, String name, MobEffect effect)
    {
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), effect);
        return effect;
    }

    private static Potion registerPotion(BiConsumer<ResourceLocation, Potion> func, String name, Potion potion)
    {
        func.accept(new ResourceLocation(TANAPI.MOD_ID, name), potion);
        return potion;
    }
}
