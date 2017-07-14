package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.temperature.Temperature;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureTrend;

public class WeatherModifier extends TemperatureModifier {
	public static final int WET_RATE_MODIFIER = -750;
	public final int WET_TARGET_MODIFIER;
	public final int SNOW_TARGET_MODIFIER;

	public WeatherModifier(TemperatureDebugger debugger) {
		super(debugger);
		this.WET_TARGET_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.WET_TEMP_MODIFIER);
		this.SNOW_TARGET_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.SNOW_TEMP_MODIFIER);
	}

	@Override
	public int modifyChangeRate(World world, EntityPlayer player,
			int changeRate, TemperatureTrend trend) {
		int newChangeRate = changeRate;

		debugger.start(Modifier.WET_RATE, changeRate);

		if (player.isWet()) {
			newChangeRate += WET_RATE_MODIFIER;
		}

		debugger.end(newChangeRate);

		return newChangeRate;
	}

	@Override
	public Temperature modifyTarget(World world, EntityPlayer player,
			Temperature temperature) {
		int temperatureLevel = temperature.getRawValue();
		int newTemperatureLevel = temperatureLevel;

		BlockPos playerPos = player.getPosition();

		if (player.isWet()) {
			debugger.start(Modifier.WET_TARGET, newTemperatureLevel);
			newTemperatureLevel += WET_TARGET_MODIFIER;
			debugger.end(newTemperatureLevel);
		} else if (world.isRaining() && world.canSeeSky(playerPos)
				&& world.getBiomeGenForCoords(playerPos).getEnableSnow()) {
			debugger.start(Modifier.SNOW_TARGET, newTemperatureLevel);
			newTemperatureLevel += SNOW_TARGET_MODIFIER;
			debugger.end(newTemperatureLevel);
		}

		return new Temperature(newTemperatureLevel);
	}

	public Temperature modifyTarget(World world, BlockPos position,
			Temperature temperature) {
		int temperatureLevel = temperature.getRawValue();
		int newTemperatureLevel = temperatureLevel;
		boolean rainChill = SyncedConfig
				.getBooleanValue(GameplayOption.RAIN_CHILL);

		if (world.isRaining() && world.canSeeSky(position)) {
			Biome biome = world.getBiomeGenForCoords(position);
			if (biome.getEnableSnow()) {
				newTemperatureLevel += SNOW_TARGET_MODIFIER;
			} else if (biome.canRain() && rainChill) {
				newTemperatureLevel += WET_TARGET_MODIFIER;
			}
		}

		return new Temperature(newTemperatureLevel);
	}
}
