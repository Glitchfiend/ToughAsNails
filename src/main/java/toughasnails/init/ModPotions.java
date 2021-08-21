/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import toughasnails.api.potion.TANEffects;
import toughasnails.potion.ThirstEffect;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPotions
{
    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<MobEffect> event)
    {
        register("thirst", new ThirstEffect(MobEffectCategory.HARMFUL, 0x76DB4C));
        register("ice_resistance", new MobEffect(MobEffectCategory.BENEFICIAL, 0x77A9FF));
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event)
    {
        register("ice_resistance", new Potion(new MobEffectInstance(TANEffects.ICE_RESISTANCE, 1200)));
        register("long_ice_resistance", new Potion(new MobEffectInstance(TANEffects.ICE_RESISTANCE, 2400)));
    }

    public static void register(String name, MobEffect effect)
    {
        effect.setRegistryName(name);
        ForgeRegistries.MOB_EFFECTS.register(effect);
    }

    public static void register(String name, Potion potion)
    {
        potion.setRegistryName(name);
        ForgeRegistries.POTIONS.register(potion);
    }
}
