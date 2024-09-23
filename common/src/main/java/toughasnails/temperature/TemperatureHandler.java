/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.temperature;

import toughasnails.glitch.event.entity.LivingEntityUseItemEvent;
import toughasnails.glitch.event.player.PlayerEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import toughasnails.api.damagesource.TANDamageTypes;
import toughasnails.api.potion.TANEffects;
import toughasnails.api.temperature.ITemperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModConfig;
import toughasnails.init.ModPackets;
import toughasnails.init.ModTags;
import toughasnails.network.UpdateTemperaturePacket;

import java.util.HashSet;
import java.util.UUID;

public class TemperatureHandler
{
    private static final UUID SPEED_MODIFIER_HYPERTHERMIA_UUID = UUID.fromString("30b6ca4e-c6df-4532-80db-1d024765b56b");

    public static void onPlayerTick(Player player)
    {
        if (!ModConfig.temperature.enableTemperature || player.level().isClientSide())
            return;

        Level level = player.level();
        ITemperature data = TemperatureHelper.getTemperatureData(player);

        // Remove thermoregulators more than 32 blocks away
        data.getNearbyThermoregulators().removeIf(pos -> level.getBlockEntity(pos) == null || player.distanceToSqr((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) > 32 * 32);

        // Decrement the positional change delay ticks
        data.setChangeDelayTicks(Math.max(0, data.getChangeDelayTicks() - 1));
        // Increment dry ticks
        data.setDryTicks(data.getDryTicks() + 1);

        // Use the positional temperature as the new target level if the player doesn't have climate clemency active
        if (!player.hasEffect(TANEffects.CLIMATE_CLEMENCY))
        {
            int changeDelay = ModConfig.temperature.temperatureChangeDelay;
            TemperatureLevel currentTargetLevel = data.getTargetLevel();
            TemperatureLevel newTargetLevel = TemperatureHelper.getTemperatureAtPos(player.level(), player.blockPosition());

            // Apply modifiers in the configured order
            for (BuiltInTemperatureModifier modifier : BuiltInTemperatureModifier.getTemperatureModifierOrder())
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
                    changeDelay = Math.min(changeDelay, ModConfig.temperature.extremityReboundTemperatureChangeDelay);
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
            data.setExtremityDelayTicks(ModConfig.temperature.extremityDamageDelay);
        }

        int hyperthermicTicks = data.getHyperthermiaTicks();
        int ticksToHyperthermia = TemperatureHelper.getTicksRequiredForHyperthermia();

        // Don't perform extremity effects in creative or spectator modes
        if (!player.isCreative() && !player.isSpectator())
        {
            // Increase hyperthermia ticks if hot
            if (!player.hasEffect(MobEffects.FIRE_RESISTANCE) && data.getLevel() == TemperatureLevel.HOT && data.getExtremityDelayTicks() == 0)
            {
                data.setHyperthermiaTicks(Math.min(ticksToHyperthermia, hyperthermicTicks + 1));
            }
            else data.setHyperthermiaTicks(Math.max(0, hyperthermicTicks - 2));
        }
        else
        {
            // Decrease hyperthermia, if present
            if (data.getHyperthermiaTicks() > 0)
                data.setHyperthermiaTicks(Math.max(0, hyperthermicTicks - 2));
        }

        removeHeatExhaustion(player);
        tryAddHeatExhaustion(player);

        // Hurt the player if hyperthermic
        if (player.tickCount % 40 == 0 && TemperatureHelper.isFullyHyperthermic(player))
            player.hurt(player.damageSources().source(TANDamageTypes.HYPERTHERMIA), 1);
    }

    public static void onChangeDimension(PlayerEvent.ChangeDimension event)
    {
        ITemperature temperature = TemperatureHelper.getTemperatureData(event.getPlayer());
        temperature.setLastLevel(TemperatureData.DEFAULT_LEVEL);
        temperature.setLastHyperthermiaTicks(0);
        temperature.setLastNearbyThermoregulators(new HashSet<>());
    }

    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (!ModConfig.temperature.enableTemperature || !(event.getEntity() instanceof Player) || event.getEntity().level().isClientSide())
            return;

        Player player = (Player) event.getEntity();
        ItemStack item = event.getItem();

        if (item.is(ModTags.Items.COOLING_CONSUMED_ITEMS))
            player.addEffect(new MobEffectInstance(TANEffects.INTERNAL_CHILL, ModConfig.temperature.consumableEffectDuration, 0, false, false, true));

        if (item.is(ModTags.Items.HEATING_CONSUMED_ITEMS))
            player.addEffect(new MobEffectInstance(TANEffects.INTERNAL_WARMTH, ModConfig.temperature.consumableEffectDuration, 0, false, false, true));
    }

    public static void syncTemperature(ServerPlayer player)
    {
        ITemperature temperature = TemperatureHelper.getTemperatureData(player);
        ModPackets.HANDLER.sendToPlayer(new UpdateTemperaturePacket(temperature.getLevel(), temperature.getHyperthermiaTicks(), temperature.getNearbyThermoregulators()), player);
        temperature.setLastLevel(temperature.getLevel());
        temperature.setLastHyperthermiaTicks(temperature.getHyperthermiaTicks());
        temperature.setNearbyThermoregulators(temperature.getNearbyThermoregulators());
    }

    private static void removeHeatExhaustion(Player player)
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

    protected static void tryAddHeatExhaustion(Player player)
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
