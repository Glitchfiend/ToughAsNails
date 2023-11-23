/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.temperature.IPlayerTemperatureModifier;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.config.TemperatureConfig;

import static toughasnails.temperature.TemperatureHelperImpl.playerModifiers;

public enum BuiltInTemperatureModifier
{
    PLAYER_MODIFIERS(((player, currentTarget, currentChangeDelay) -> {
        TemperatureLevel newTarget = currentTarget;
        int newChangeDelay = currentChangeDelay;
        for (IPlayerTemperatureModifier modifier : playerModifiers)
        {
            newTarget = modifier.modify(player, currentTarget);
        }
        if (newTarget != currentTarget) newChangeDelay = Math.min(currentChangeDelay, TemperatureConfig.playerTemperatureChangeDelay.get());
        return new Tuple<>(newTarget, newChangeDelay);
    })),
    ITEM_MODIFIER((player, currentTarget, currentChangeDelay) -> {
        int newChangeDelay = currentChangeDelay;
        TemperatureLevel newTarget = TemperatureHelperImpl.handheldModifier(player, currentTarget);
        if (newTarget != currentTarget) newChangeDelay = Math.min(currentChangeDelay, TemperatureConfig.handheldTemperatureChangeDelay.get());
        return new Tuple<>(newTarget, newChangeDelay);
    }),
    ARMOR_MODIFIER((player, currentTarget, currentChangeDelay) -> {
        int newChangeDelay = currentChangeDelay;
        TemperatureLevel newTarget = TemperatureHelperImpl.armorModifier(player, currentTarget);
        if (newTarget != currentTarget) newChangeDelay = Math.min(currentChangeDelay, TemperatureConfig.armorTemperatureChangeDelay.get());
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
}
