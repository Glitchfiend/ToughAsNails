/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.temperature.IPlayerTemperatureModifier;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModConfig;

import java.util.List;

import static toughasnails.temperature.TemperatureHelperImpl.playerModifiers;

public enum BuiltInTemperatureModifier
{
    PLAYER_MODIFIERS(((player, currentTarget, currentChangeDelay) -> {
        TemperatureLevel newTarget = currentTarget;
        int newChangeDelay = currentChangeDelay;
        for (IPlayerTemperatureModifier modifier : playerModifiers)
        {
            newTarget = modifier.modify(player, newTarget);
        }

        if (newTarget != currentTarget) newChangeDelay = Math.min(currentChangeDelay, ModConfig.temperature.playerTemperatureChangeDelay);
        return new Pair<>(newTarget, newChangeDelay);
    })),
    ITEM_MODIFIER((player, currentTarget, currentChangeDelay) -> {
        int newChangeDelay = currentChangeDelay;
        TemperatureLevel newTarget = TemperatureHelperImpl.handheldModifier(player, currentTarget);
        if (newTarget != currentTarget) newChangeDelay = Math.min(currentChangeDelay, ModConfig.temperature.handheldTemperatureChangeDelay);
        return new Pair<>(newTarget, newChangeDelay);
    }),
    ARMOR_MODIFIER((player, currentTarget, currentChangeDelay) -> {
        int newChangeDelay = currentChangeDelay;
        TemperatureLevel newTarget = TemperatureHelperImpl.armorModifier(player, currentTarget);
        if (newTarget != currentTarget) newChangeDelay = Math.min(currentChangeDelay, ModConfig.temperature.armorTemperatureChangeDelay);
        return new Pair<>(newTarget, newChangeDelay);
    }),
    MOUNT_MODIFIER((player, currentTarget, currentChangeDelay) -> {
        int newChangeDelay = currentChangeDelay;
        TemperatureLevel newTarget = TemperatureHelperImpl.mountModifier(player, currentTarget);
        if (newTarget != currentTarget) newChangeDelay = Math.min(currentChangeDelay, ModConfig.temperature.mountTemperatureChangeDelay);
        return new Pair<>(newTarget, newChangeDelay);
    }),
    INTERNAL_MODIFIER((player, currentTarget, currentChangeDelay) -> {
        int newChangeDelay = currentChangeDelay;
        TemperatureLevel newTarget = TemperatureHelperImpl.internalModifier(player, currentTarget);
        if (newTarget != currentTarget) newChangeDelay = Math.min(currentChangeDelay, ModConfig.temperature.internalTemperatureChangeDelay);
        return new Pair<>(newTarget, newChangeDelay);
    });

    private final Modifier modifier;

    BuiltInTemperatureModifier(Modifier modifier)
    {
        this.modifier = modifier;
    }

    public Pair<TemperatureLevel, Integer> apply(Player player, TemperatureLevel currentTarget, int currentChangeDelay)
    {
        return this.modifier.apply(player, currentTarget, currentChangeDelay);
    }

    private interface Modifier
    {
        public Pair<TemperatureLevel, Integer> apply(Player player, TemperatureLevel currentTarget, int currentChangeDelay);
    }

    private static List<BuiltInTemperatureModifier> temperatureModifierOrderCache;
    public static List<BuiltInTemperatureModifier> getTemperatureModifierOrder()
    {
        if (temperatureModifierOrderCache == null)
        {
            temperatureModifierOrderCache = ModConfig.temperature.temperatureModifierOrder.stream().map(s -> BuiltInTemperatureModifier.valueOf(s.toUpperCase())).toList();
        }
        return temperatureModifierOrderCache;
    }
}
