/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.init;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import toughasnails.potion.ThirstEffect;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPotions
{
    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<Effect> event)
    {
        register("thirst", new ThirstEffect(EffectType.HARMFUL, 0x76DB4C));
    }

    public static void register(String name, Effect effect)
    {
        effect.setRegistryName(name);
        ForgeRegistries.POTIONS.register(effect);
    }
}
