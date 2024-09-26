/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import static toughasnails.temperature.TemperatureHelperImpl.playerModifiers;

import java.util.List;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.temperature.IPlayerTemperatureModifier;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.init.ModConfig;

public enum BuiltInTemperatureModifier
{
    PLAYER_MODIFIERS(((player, currentTarget, currentChangeDelay) -> {
        TemperatureLevel newTarget = currentTarget;
        int newChangeDelay = currentChangeDelay;
        for (IPlayerTemperatureModifier modifier : playerModifiers)
        {
            newTarget = modifier.modify(player, newTarget);
        }

        if (newTarget != currentTarget) newChangeDelay = Math.min(currentChangeDelay, ModConfig.temperature.playerTemperatureChangeDelay());
        return new Tuple<>(newTarget, newChangeDelay);
    })),
    ITEM_MODIFIER((player, currentTarget, currentChangeDelay) -> {
        int newChangeDelay = currentChangeDelay;
        TemperatureLevel newTarget = TemperatureHelperImpl.handheldModifier(player, currentTarget);
        if (newTarget != currentTarget) newChangeDelay = Math.min(currentChangeDelay, ModConfig.temperature.handheldTemperatureChangeDelay());
        return new Tuple<>(newTarget, newChangeDelay);
    }),
    ARMOR_MODIFIER((player, currentTarget, currentChangeDelay) -> {
        int newChangeDelay = currentChangeDelay;
        TemperatureLevel newTarget = TemperatureHelperImpl.armorModifier(player, currentTarget);
        if (newTarget != currentTarget) newChangeDelay = Math.min(currentChangeDelay, ModConfig.temperature.armorTemperatureChangeDelay());
        return new Tuple<>(newTarget, newChangeDelay);
    }),
    INTERNAL_MODIFIER((player, currentTarget, currentChangeDelay) -> {
        int newChangeDelay = currentChangeDelay;
        TemperatureLevel newTarget = TemperatureHelperImpl.internalModifier(player, currentTarget);
        if (newTarget != currentTarget) newChangeDelay = Math.min(currentChangeDelay, ModConfig.temperature.internalTemperatureChangeDelay());
        return new Tuple<>(newTarget, newChangeDelay);
    });

    private final Modifier modifier;

    BuiltInTemperatureModifier(Modifier modifier)
    {
        this.modifier = modifier;
    }

    public Tuple<TemperatureLevel, Integer> apply(Player player, TemperatureLevel currentTarget, int currentChangeDelay)
    {
        return this.modifier.apply(player, currentTarget, currentChangeDelay);
    }

    private interface Modifier
    {
        public Tuple<TemperatureLevel, Integer> apply(Player player, TemperatureLevel currentTarget, int currentChangeDelay);
    }

    private static List<BuiltInTemperatureModifier> temperatureModifierOrderCache;
    public static List<BuiltInTemperatureModifier> getTemperatureModifierOrder()
    {
        if (temperatureModifierOrderCache == null)
        {
            temperatureModifierOrderCache = ModConfig.temperature.temperatureModifierOrder().stream().map(s -> BuiltInTemperatureModifier.valueOf(s.toUpperCase())).toList();
        }
        return temperatureModifierOrderCache;
    }
}
