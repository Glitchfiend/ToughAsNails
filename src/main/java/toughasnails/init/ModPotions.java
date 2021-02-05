/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
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
        registerEffect(new ThirstEffect(EffectType.HARMFUL, 0x76DB4C), "thirst");
    }

    public static Effect registerEffect(Effect effect, String name)
    {
        effect.setRegistryName(name);
        ForgeRegistries.POTIONS.register(effect);
        return effect;
    }
}
