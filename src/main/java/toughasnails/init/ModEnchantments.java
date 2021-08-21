/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import toughasnails.api.potion.TANEffects;
import toughasnails.enchantment.CoolingEnchantment;
import toughasnails.enchantment.WarmingEnchantment;
import toughasnails.potion.ThirstEffect;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEnchantments
{
    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event)
    {
        register("cooling", new CoolingEnchantment());
        register("warming", new WarmingEnchantment());
    }

    public static void register(String name, Enchantment enchantment)
    {
        enchantment.setRegistryName(name);
        ForgeRegistries.ENCHANTMENTS.register(enchantment);
    }
}
