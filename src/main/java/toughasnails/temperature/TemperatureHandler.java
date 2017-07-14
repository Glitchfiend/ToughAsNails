package toughasnails.temperature;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import toughasnails.api.TANCapabilities;
import toughasnails.api.TANPotions;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.stat.StatHandlerBase;
import toughasnails.api.stat.capability.ITemperature;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.api.temperature.TemperatureScale.TemperatureRange;
import toughasnails.network.message.MessageUpdateStat;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.modifier.AltitudeModifier;
import toughasnails.temperature.modifier.ArmorModifier;
import toughasnails.temperature.modifier.BiomeModifier;
import toughasnails.temperature.modifier.ObjectProximityModifier;
import toughasnails.temperature.modifier.PlayerStateModifier;
import toughasnails.temperature.modifier.SeasonModifier;
import toughasnails.temperature.modifier.TemperatureModifier;
import toughasnails.temperature.modifier.TemperatureModifier.ExternalModifier;
import toughasnails.temperature.modifier.TimeModifier;
import toughasnails.temperature.modifier.WeatherModifier;

public class TemperatureHandler extends StatHandlerBase
		implements ITemperature {
	public static final int TEMPERATURE_SCALE_MIDPOINT = TemperatureScale
			.getScaleTotal() / 2;
	public static final int BASE_TEMPERATURE_CHANGE_TICKS = 1200;

	private int temperatureLevel;
	private int prevTemperatureLevel;
	private int temperatureTimer;

	private TemperatureModifier altitudeModifier;
	private TemperatureModifier armorModifier;
	private TemperatureModifier biomeModifier;
	private TemperatureModifier playerStateModifier;
	private TemperatureModifier objectProximityModifier;
	private TemperatureModifier weatherModifier;
	private TemperatureModifier timeModifier;
	private TemperatureModifier seasonModifier;

	private Map<String, TemperatureModifier.ExternalModifier> externalModifiers;

	public final TemperatureDebugger debugger = new TemperatureDebugger();

	public TemperatureHandler() {
		this.temperatureLevel = TemperatureScale.getScaleTotal() / 2;
		this.prevTemperatureLevel = this.temperatureLevel;

		this.altitudeModifier = new AltitudeModifier(debugger);
		this.armorModifier = new ArmorModifier(debugger);
		this.biomeModifier = new BiomeModifier(debugger);
		this.playerStateModifier = new PlayerStateModifier(debugger);
		this.objectProximityModifier = new ObjectProximityModifier(debugger);
		this.weatherModifier = new WeatherModifier(debugger);
		this.timeModifier = new TimeModifier(debugger);
		this.seasonModifier = new SeasonModifier(debugger);

		this.externalModifiers = Maps.newHashMap();
	}

	@Override
	public void update(EntityPlayer player, World world, Phase phase) {
		if (phase == Phase.END && !world.isRemote) {
			int newTempChangeTicks = BASE_TEMPERATURE_CHANGE_TICKS;

			TemperatureTrend trend;

			if (debugger.targetTemperature == this.temperatureLevel)
				trend = TemperatureTrend.STILL;
			else
				trend = debugger.targetTemperature > this.temperatureLevel
						? TemperatureTrend.INCREASING
						: TemperatureTrend.DECREASING;

			newTempChangeTicks = altitudeModifier.modifyChangeRate(world,
					player, newTempChangeTicks, trend);
			newTempChangeTicks = armorModifier.modifyChangeRate(world, player,
					newTempChangeTicks, trend);
			newTempChangeTicks = biomeModifier.modifyChangeRate(world, player,
					newTempChangeTicks, trend);
			newTempChangeTicks = playerStateModifier.modifyChangeRate(world,
					player, newTempChangeTicks, trend);
			newTempChangeTicks = objectProximityModifier.modifyChangeRate(world,
					player, newTempChangeTicks, trend);
			newTempChangeTicks = weatherModifier.modifyChangeRate(world, player,
					newTempChangeTicks, trend);
			newTempChangeTicks = timeModifier.modifyChangeRate(world, player,
					newTempChangeTicks, trend);

			java.util.Iterator<TemperatureModifier.ExternalModifier> iterator = this.externalModifiers
					.values().iterator();

			debugger.start(Modifier.CLIMATISATION_RATE, newTempChangeTicks);
			while (iterator.hasNext()) {
				TemperatureModifier.ExternalModifier modifier = iterator.next();

				if (this.temperatureTimer > modifier.getEndTime()) {
					iterator.remove();
				} else {
					if (SyncedConfig.getBooleanValue(
							GameplayOption.ENABLE_TEMPERATURE)) {
						newTempChangeTicks += modifier.getRate();
					}
				}
			}
			debugger.end(newTempChangeTicks);

			newTempChangeTicks = Math.max(20, newTempChangeTicks);

			boolean incrementTemperature = ++temperatureTimer >= newTempChangeTicks;
			boolean updateClient = ++debugger.debugTimer % 5 == 0;

			debugger.temperatureTimer = temperatureTimer;
			debugger.changeTicks = newTempChangeTicks;

			if (incrementTemperature && SyncedConfig
					.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE)) {
				for (ExternalModifier modifier : this.externalModifiers
						.values()) {
					modifier.setEndTime(
							modifier.getEndTime() - this.temperatureTimer);
				}
			}

			if ((incrementTemperature || updateClient) && SyncedConfig
					.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE)) {
				debugger.start(Modifier.EQUILIBRIUM_TARGET, 0);
				debugger.end(TemperatureScale.getScaleTotal() / 2);

				Temperature targetTemperature = biomeModifier.modifyTarget(
						world, player,
						new Temperature(TEMPERATURE_SCALE_MIDPOINT));
				targetTemperature = altitudeModifier.modifyTarget(world, player,
						targetTemperature);
				targetTemperature = armorModifier.modifyTarget(world, player,
						targetTemperature);
				targetTemperature = playerStateModifier.modifyTarget(world,
						player, targetTemperature);
				targetTemperature = objectProximityModifier.modifyTarget(world,
						player, targetTemperature);
				targetTemperature = weatherModifier.modifyTarget(world, player,
						targetTemperature);
				targetTemperature = timeModifier.modifyTarget(world, player,
						targetTemperature);
				targetTemperature = seasonModifier.modifyTarget(world, player,
						targetTemperature);

				debugger.start(Modifier.CLIMATISATION_TARGET,
						targetTemperature.getRawValue());
				for (TemperatureModifier.ExternalModifier modifier : this.externalModifiers
						.values()) {
					targetTemperature = new Temperature(
							targetTemperature.getRawValue()
									+ modifier.getAmount());
				}
				debugger.end(targetTemperature.getRawValue());

				debugger.targetTemperature = targetTemperature.getRawValue();

				targetTemperature = new Temperature(
						MathHelper.clamp_int(targetTemperature.getRawValue(), 0,
								TemperatureScale.getScaleTotal()));

				if (incrementTemperature) {
					this.addTemperature(new Temperature(
							(int) Math.signum(targetTemperature.getRawValue()
									- this.temperatureLevel)));
					this.temperatureTimer = 0;
				}
			}

			addPotionEffects(player);

			if (updateClient) {
				// This works because update is only called if !world.isRemote
				debugger.finalize((EntityPlayerMP) player);
			}
		}
	}

	private void addPotionEffects(EntityPlayer player) {
		// TemperatureRange range = TemperatureScale
		// .getTemperatureRange(this.temperatureLevel);
		float multiplier = 1.0F;

		// The point from 0 to 1 at which potion effects begin in an extremity
		// range
		float extremityDelta = (3.0F / 6.0F);

		// Start the hypo/hyperthermia slightly after the real ranges start
		int hypoRangeSize = (int) (TemperatureRange.ICY.getRangeSize()
				* extremityDelta);
		int hypoRangeStart = hypoRangeSize - 1;
		int hyperRangeSize = (int) (TemperatureRange.HOT.getRangeSize()
				* extremityDelta);
		int hyperRangeStart = (TemperatureScale.getScaleTotal() + 1)
				- hyperRangeSize;

		// Don't apply any negative effects whilst in creative mode
		if (!player.capabilities.isCreativeMode && (SyncedConfig
				.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE))) {
			if (this.temperatureLevel <= hypoRangeStart
					&& (!player.isPotionActive(TANPotions.cold_resistance))
					&& (temperatureLevel < prevTemperatureLevel || !player
							.isPotionActive(TANPotions.hypothermia))) {
				multiplier = 1.0F - ((float) (this.temperatureLevel + 1)
						/ (float) hypoRangeSize);
				player.removePotionEffect(TANPotions.hypothermia);
				player.addPotionEffect(new PotionEffect(TANPotions.hypothermia,
						(int) (1800 * multiplier) + 600,
						(int) (3 * multiplier + extremityDelta)));
			} else if (this.temperatureLevel >= hyperRangeStart
					&& (!player.isPotionActive(TANPotions.heat_resistance))
					&& (temperatureLevel > prevTemperatureLevel || !player
							.isPotionActive(TANPotions.hyperthermia))) {
				multiplier = (float) (this.temperatureLevel - hyperRangeStart)
						/ hyperRangeSize;
				player.removePotionEffect(TANPotions.hyperthermia);
				player.addPotionEffect(new PotionEffect(TANPotions.hyperthermia,
						(int) (1800 * multiplier) + 600,
						(int) (3 * multiplier)));
			}
		}
	}

	@Override
	public boolean hasChanged() {
		return this.prevTemperatureLevel != this.temperatureLevel;
	}

	@Override
	public void onSendClientUpdate() {
		this.prevTemperatureLevel = this.temperatureLevel;
	}

	@Override
	public IMessage createUpdateMessage() {
		NBTTagCompound data = (NBTTagCompound) TANCapabilities.TEMPERATURE
				.getStorage().writeNBT(TANCapabilities.TEMPERATURE, this, null);
		return new MessageUpdateStat(TANCapabilities.TEMPERATURE, data);
	}

	@Override
	public void setChangeTime(int ticks) {
		this.temperatureTimer = ticks;
	}

	@Override
	public int getChangeTime() {
		return this.temperatureTimer;
	}

	@Override
	public void setTemperature(Temperature temperature) {
		this.temperatureLevel = temperature.getRawValue();
	}

	@Override
	public void addTemperature(Temperature difference) {
		this.temperatureLevel = Math
				.max(Math.min(TemperatureScale.getScaleTotal(),
						this.temperatureLevel + difference.getRawValue()), 0);
	}

	@Override
	public void applyModifier(String name, int amount, int rate, int duration) {
		if (this.externalModifiers.containsKey(name)) {
			ExternalModifier modifier = this.externalModifiers.get(name);
			modifier.setEndTime(this.temperatureTimer + duration);
		} else {
			TemperatureModifier.ExternalModifier modifier = new TemperatureModifier.ExternalModifier(
					name, amount, rate, this.temperatureTimer + duration);
			this.externalModifiers.put(name, modifier);
		}
	}

	@Override
	public boolean hasModifier(String name) {
		return this.externalModifiers.containsKey(name);
	}

	@Override
	public ImmutableMap<String, ExternalModifier> getExternalModifiers() {
		return ImmutableMap.copyOf(this.externalModifiers);
	}

	@Override
	public void setExternalModifiers(
			Map<String, ExternalModifier> externalModifiers) {
		this.externalModifiers = externalModifiers;
	}

	@Override
	public Temperature getTemperature() {
		return new Temperature(this.temperatureLevel);
	}

	public static int getTargetTemperatureAt(World world, BlockPos position) {
		final TemperatureDebugger debugger = new TemperatureDebugger();
		AltitudeModifier altitudeModifier = new AltitudeModifier(debugger);
		BiomeModifier biomeModifier = new BiomeModifier(debugger);
		ObjectProximityModifier objectProximityModifier = new ObjectProximityModifier(
				debugger);
		WeatherModifier weatherModifier = new WeatherModifier(debugger);
		TimeModifier timeModifier = new TimeModifier(debugger);
		SeasonModifier seasonModifier = new SeasonModifier(debugger);

		Temperature baseTemperature = new Temperature(
				TemperatureHandler.TEMPERATURE_SCALE_MIDPOINT);
		Temperature targetTemperature = biomeModifier.modifyTarget(world,
				position, baseTemperature);
		targetTemperature = altitudeModifier.modifyTarget(world, position,
				targetTemperature);
		targetTemperature = objectProximityModifier.modifyTarget(world,
				position, targetTemperature);
		targetTemperature = weatherModifier.modifyTarget(world, position,
				targetTemperature);
		targetTemperature = timeModifier.modifyTarget(world, position,
				targetTemperature);
		targetTemperature = seasonModifier.modifyTarget(world, position,
				targetTemperature);

		int finalTemperature = targetTemperature.getRawValue();
		return finalTemperature;
	}
}
