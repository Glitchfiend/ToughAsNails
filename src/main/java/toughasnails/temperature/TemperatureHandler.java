/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import toughasnails.api.capability.TANCapabilities;
import toughasnails.api.temperature.IPositionalTemperatureModifier;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.config.ServerConfig;
import toughasnails.core.ToughAsNails;
import toughasnails.network.MessageUpdateTemperature;
import toughasnails.network.PacketHandler;
import toughasnails.thirst.ThirstCapabilityProvider;
import toughasnails.thirst.ThirstData;

import java.util.List;

public class TemperatureHandler
{
    private static TemperatureLevel lastSentTemperature = null;

    // Positional:
    // TODO: Offset by nearby heat sources
    // Player:
    // TODO: Offset by weather
    // TODO: Offset by clothing
    // TODO: Offset by in water
    // TODO: Offset when on fire
    // TODO: Potion effects
    // TODO: Armor enchantments



    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        // NOTE: We always attach the thirst capability, regardless of the thirst enabled config option.
        // This is mainly to ensure a consistent working environment
        if (event.getObject() instanceof Player)
        {
            event.addCapability(new ResourceLocation(ToughAsNails.MOD_ID, "temperature"), new TemperatureCapabilityProvider(TANCapabilities.TEMPERATURE, new TemperatureData()));
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getPlayer().level.isClientSide())
            return;

        syncTemperature((ServerPlayer)event.getPlayer());
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (!ServerConfig.enableTemperature.get() || event.player.level.isClientSide())
            return;

        ServerPlayer player = (ServerPlayer)event.player;
        ITemperature data = TemperatureHelper.getTemperatureData(player);
        TemperatureLevel newLevel = TemperatureHelper.getTemperatureAtPos(player.getLevel(), player.blockPosition());

        // Update the player's temperature to the new level
        data.setLevel(newLevel);

        // Update the temperature if it has changed
        if (lastSentTemperature != data.getLevel())
        {
            syncTemperature(player);
            lastSentTemperature = data.getLevel();
        }

        int frozenTicks = player.getTicksFrozen();
        int ticksToFreeze = player.getTicksRequiredToFreeze() + 2; // Add 2 to cause damage

        if (data.getLevel() == TemperatureLevel.ICY && frozenTicks < ticksToFreeze)
        {
            // Increment ticks frozen by 3 to overcome thawing.
            player.setTicksFrozen(Math.min(ticksToFreeze, player.getTicksFrozen() + 3));
        }
    }

    private static void syncTemperature(ServerPlayer player)
    {
        TemperatureLevel temperature = TemperatureHelper.getTemperatureForPlayer(player);
        PacketHandler.HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new MessageUpdateTemperature(temperature));
    }
}
