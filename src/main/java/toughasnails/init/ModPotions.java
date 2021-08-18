/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import toughasnails.potion.ThirstEffect;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPotions
{
    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<MobEffect> event)
    {
        register("thirst", new ThirstEffect(MobEffectCategory.HARMFUL, 0x76DB4C));
    }

    public static void register(String name, MobEffect effect)
    {
        effect.setRegistryName(name);
        ForgeRegistries.POTIONS.register(effect);
    }
}
