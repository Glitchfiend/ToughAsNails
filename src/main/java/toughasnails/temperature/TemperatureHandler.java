/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import com.google.common.collect.Lists;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import toughasnails.api.capability.TANCapabilities;
import toughasnails.api.damagesource.TANDamageSources;
import toughasnails.api.temperature.*;
import toughasnails.config.ServerConfig;
import toughasnails.core.ToughAsNails;
import toughasnails.network.MessageUpdateTemperature;
import toughasnails.network.PacketHandler;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static toughasnails.temperature.TemperatureHelperImpl.DATA_TICKS_HYPERTHERMIC;
import static toughasnails.temperature.TemperatureHelperImpl.playerModifiers;

public class TemperatureHandler
{
    private static final UUID SPEED_MODIFIER_HYPERTHERMIA_UUID = UUID.fromString("30b6ca4e-c6df-4532-80db-1d024765b56b");

    private static TemperatureLevel lastSentTemperature = null;

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event)
    {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player)event.getEntity();
        player.getEntityData().define(DATA_TICKS_HYPERTHERMIC, 0);
    }

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
    public void onPlayerCloned(PlayerEvent.Clone event)
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

        for (IPlayerTemperatureModifier modifier : playerModifiers)
        {
            newLevel = modifier.modify(player, newLevel);
        }

        // Update the player's temperature to the new level
        data.setLevel(newLevel);

        // Update the temperature if it has changed
        if (lastSentTemperature != data.getLevel())
        {
            syncTemperature(player);
        }

        int frozenTicks = player.getTicksFrozen();
        int ticksToFreeze = player.getTicksRequiredToFreeze() + 2; // Add 2 to cause damage

        if (!player.isCreative() && data.getLevel() == TemperatureLevel.ICY && frozenTicks < ticksToFreeze)
        {
            player.setTicksFrozen(Math.min(ticksToFreeze, player.getTicksFrozen() + 2));
        }

        int hyperthermicTicks = TemperatureHelper.getTicksHyperthermic(player);
        int ticksToHyperthermia = TemperatureHelper.getTicksRequiredForHyperthermia();

        // Increase hyperthermia ticks if hot
        if (!player.isCreative() && data.getLevel() == TemperatureLevel.HOT)
        {
            TemperatureHelper.setTicksHyperthermic(player, Math.min(ticksToHyperthermia, hyperthermicTicks + 1));

            if (player.getTicksFrozen() > 0)
                player.setTicksFrozen(Math.max(0, player.getTicksFrozen() - 2));
        }
        else
            TemperatureHelper.setTicksHyperthermic(player, Math.max(0, hyperthermicTicks - 2));

        removeHeatExhaustion(player);
        tryAddHeatExhaustion(player);

        // Hurt the player if hyperthermic
        if (player.tickCount % 40 == 0 && TemperatureHelper.isFullyHyperthermic(player))
            player.hurt(TANDamageSources.HYPERTHERMIA, 1);
    }

    private static void syncTemperature(ServerPlayer player)
    {
        TemperatureLevel temperature = TemperatureHelper.getTemperatureForPlayer(player);
        lastSentTemperature = temperature;
        PacketHandler.HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new MessageUpdateTemperature(temperature));
    }

    private static void removeHeatExhaustion(ServerPlayer player)
    {
        AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (attribute != null)
        {
            if (attribute.getModifier(SPEED_MODIFIER_HYPERTHERMIA_UUID) != null)
            {
                attribute.removeModifier(SPEED_MODIFIER_HYPERTHERMIA_UUID);
            }
        }
    }

    protected void tryAddHeatExhaustion(ServerPlayer player)
    {
        if (!player.level.getBlockState(player.getOnPos()).isAir())
        {
            int ticks = TemperatureHelper.getTicksHyperthermic(player);
            if (ticks > 0)
            {
                AttributeInstance attributeinstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attributeinstance == null)
                    return;

                float f = -0.05F * TemperatureHelper.getPercentHyperthermic(player);
                attributeinstance.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_HYPERTHERMIA_UUID, "Hyperthermia slow", (double)f, AttributeModifier.Operation.ADDITION));
            }
        }
    }
}
