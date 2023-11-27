/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import toughasnails.api.capability.TANCapabilities;
import toughasnails.api.damagesource.TANDamageTypes;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.config.ServerConfig;
import toughasnails.config.TemperatureConfig;
import toughasnails.core.ToughAsNails;
import toughasnails.network.MessageUpdateTemperature;
import toughasnails.network.PacketHandler;

import java.util.UUID;

public class TemperatureHandler
{
    private static final UUID SPEED_MODIFIER_HYPERTHERMIA_UUID = UUID.fromString("30b6ca4e-c6df-4532-80db-1d024765b56b");

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
        if (event.getEntity().level().isClientSide())
            return;

        syncTemperature((ServerPlayer)event.getEntity());
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        if (event.getEntity().level().isClientSide())
            return;

        syncTemperature((ServerPlayer)event.getEntity());
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event)
    {
        if (event.getEntity().level().isClientSide())
            return;

        syncTemperature((ServerPlayer)event.getEntity());

        if (!TemperatureConfig.climateClemencyRespawning.get())
        {
            event.getEntity().getPersistentData().putBoolean("climateClemencyGranted", event.getOriginal().getPersistentData().getBoolean("climateClemencyGranted"));
        }
    }

    @SubscribeEvent
    public void onPlayerSpawn(EntityJoinLevelEvent event)
    {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Player))
            return;

        Player player = (Player)event.getEntity();
        CompoundTag data = player.getPersistentData();

        if (ServerConfig.enableTemperature.get() && TemperatureConfig.climateClemencyDuration.get() > 0 && !data.getBoolean("climateClemencyGranted") && !player.isCreative())
        {
            data.putBoolean("climateClemencyGranted", true);
            player.addEffect(new MobEffectInstance(TANEffects.CLIMATE_CLEMENCY.get(), TemperatureConfig.climateClemencyDuration.get(), 0, false, false, true));
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (!ServerConfig.enableTemperature.get() || event.player.level().isClientSide())
            return;

        ServerPlayer player = (ServerPlayer)event.player;
        ITemperature data = TemperatureHelper.getTemperatureData(player);

        // Decrement the positional change delay ticks
        data.setChangeDelayTicks(Math.max(0, data.getChangeDelayTicks() - 1));
        // Increment dry ticks
        data.setDryTicks(data.getDryTicks() + 1);

        // Use the positional temperature as the new target level if the player doesn't have climate clemency active
        if (!player.hasEffect(TANEffects.CLIMATE_CLEMENCY.get()))
        {
            int changeDelay = TemperatureConfig.temperatureChangeDelay.get();
            TemperatureLevel currentTargetLevel = data.getTargetLevel();
            TemperatureLevel newTargetLevel = TemperatureHelper.getTemperatureAtPos(player.level(), player.blockPosition());

            // Apply modifiers in the configured order
            for (BuiltInTemperatureModifier modifier : TemperatureConfig.getTemperatureModifierOrder())
            {
                Tuple<TemperatureLevel, Integer> output = modifier.apply(player, newTargetLevel, changeDelay);
                newTargetLevel = output.getA();
                changeDelay = output.getB();
            }

            // If necessary, change the target level and reset the timer
            if (newTargetLevel != currentTargetLevel)
            {
                data.setTargetLevel(newTargetLevel);

                // Rebound quickly from extremes
                if ((data.getLevel() == TemperatureLevel.ICY || data.getLevel() == TemperatureLevel.HOT) && newTargetLevel != TemperatureLevel.ICY && newTargetLevel != TemperatureLevel.HOT)
                {
                    changeDelay = Math.min(changeDelay, TemperatureConfig.extremityReboundTemperatureChangeDelay.get());
                }

                data.setChangeDelayTicks(changeDelay);
            }

            // If the delay timer is complete, and the target temperature isn't the same as the player's current temperature,
            // move the player's positional temperature level to move towards the target.
            if (data.getChangeDelayTicks() == 0 && currentTargetLevel != data.getLevel())
            {
                data.setLevel(data.getLevel().increment(Mth.sign(data.getTargetLevel().ordinal() - data.getLevel().ordinal())));
                data.setChangeDelayTicks(changeDelay);
            }
        }
        else
        {
            // Always set the temperature to neutral when climate clemency is active
            data.setLevel(TemperatureLevel.NEUTRAL);
        }

        // Decrement the extremity delay ticks
        data.setExtremityDelayTicks(Math.max(0, data.getExtremityDelayTicks() - 1));

        // If we are entering an extreme temperature, add to the extremity delay
        if (data.getLastLevel() != data.getLevel() && (data.getLevel() == TemperatureLevel.ICY || data.getLevel() == TemperatureLevel.HOT))
        {
            data.setExtremityDelayTicks(TemperatureConfig.extremityDamageDelay.get());
        }

        int hyperthermicTicks = data.getHyperthermiaTicks();
        int ticksToHyperthermia = TemperatureHelper.getTicksRequiredForHyperthermia();

        // Don't perform extremity effects in creative or spectator modes
        if (!player.isCreative() && !player.isSpectator())
        {
            // Freeze the player if they're icy
            if (!player.hasEffect(TANEffects.ICE_RESISTANCE.get()) && data.getLevel() == TemperatureLevel.ICY && data.getExtremityDelayTicks() == 0)
            {
                int frozenTicks = player.getTicksFrozen();
                int ticksToFreeze = player.getTicksRequiredToFreeze() + 2; // Add 2 to cause damage

                if (frozenTicks < ticksToFreeze)
                    player.setTicksFrozen(Math.min(ticksToFreeze, player.getTicksFrozen() + 2));
            }

            // Increase hyperthermia ticks if hot
            if (!player.hasEffect(MobEffects.FIRE_RESISTANCE) && data.getLevel() == TemperatureLevel.HOT && data.getExtremityDelayTicks() == 0)
            {
                data.setHyperthermiaTicks(Math.min(ticksToHyperthermia, hyperthermicTicks + 1));

                if (player.getTicksFrozen() > 0)
                    player.setTicksFrozen(Math.max(0, player.getTicksFrozen() - 2));
            }
            else data.setHyperthermiaTicks(Math.max(0, hyperthermicTicks - 2));
        }
        else
        {
            // Decrease hyperthermia, if present
            if (data.getHyperthermiaTicks() > 0)
                data.setHyperthermiaTicks(Math.max(0, hyperthermicTicks - 2));
        }

        // Reset frozen ticks with ice resistance. This is mainly to avoid the effects of powdered snow.
        if (player.hasEffect(TANEffects.ICE_RESISTANCE.get()) && player.getTicksFrozen() > 0)
        {
            player.setTicksFrozen(0);
        }

        // Update the temperature if it has changed
        if (data.getLastLevel() != data.getLevel() || data.getLastHyperthermiaTicks() != data.getHyperthermiaTicks())
        {
            syncTemperature(player);
        }

        removeHeatExhaustion(player);
        tryAddHeatExhaustion(player);

        // Hurt the player if hyperthermic
        if (player.tickCount % 40 == 0 && TemperatureHelper.isFullyHyperthermic(player))
            player.hurt(player.damageSources().source(TANDamageTypes.HYPERTHERMIA), 1);
    }

    private static void syncTemperature(ServerPlayer player)
    {
        ITemperature temperature = TemperatureHelper.getTemperatureData(player);
        temperature.setLastLevel(temperature.getLevel());
        temperature.setLastHyperthermiaTicks(temperature.getHyperthermiaTicks());
        PacketHandler.HANDLER.send(new MessageUpdateTemperature(temperature.getLevel(), temperature.getHyperthermiaTicks()), PacketDistributor.PLAYER.with(player));
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
        if (!player.level().getBlockState(player.getOnPos()).isAir())
        {
            int ticks = TemperatureHelper.getTicksHyperthermic(player);
            if (ticks > 0)
            {
                AttributeInstance attributeinstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attributeinstance == null)
                    return;

                float f = 0.015F * TemperatureHelper.getPercentHyperthermic(player);
                attributeinstance.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_HYPERTHERMIA_UUID, "Hyperthermia slow", (double)f, AttributeModifier.Operation.ADDITION));
            }
        }
    }
}
