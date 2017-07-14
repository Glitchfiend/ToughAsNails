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
import toughasnails.util.BiomeUtils;

public class TimeModifier extends TemperatureModifier {
	public final int TIME_TARGET_MODIFIER;

	/**
	 * Multiplies how much should the temperature be increased/decreased by the
	 * closer the biome temp is to a extreme hot or cold
	 */
	public final float EXTREMITY_MULTIPLIER;

	public TimeModifier(TemperatureDebugger debugger) {
		super(debugger);
		this.TIME_TARGET_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.TIME_TEMP_MODIFIER);
		this.EXTREMITY_MULTIPLIER = SyncedConfig
				.getFloatValue(GameplayOption.TIME_EXTREMITY_MODIFIER);
	}

	@Override
	public int modifyChangeRate(World world, EntityPlayer player,
			int changeRate, TemperatureTrend trend) {
		return changeRate;
	}

	@Override
	public Temperature modifyTarget(World world, EntityPlayer player,
			Temperature temperature) {
		Biome biome = world.getBiomeGenForCoords(player.getPosition());
		long worldTime = world.getWorldTime();

		float extremityModifier = BiomeUtils.getBiomeTempExtremity(biome);
		// Reaches the highest point during the middle of the day and at
		// midnight. Normalized to be between -1 and 1
		float timeNorm = (-Math.abs(((worldTime + 6000) % 24000.0F) - 12000.0F)
				+ 6000.0F) / 6000.0F;

		int temperatureLevel = temperature.getRawValue();
		int newTemperatureLevel = temperatureLevel;

		debugger.start(Modifier.TIME_TARGET, newTemperatureLevel);

		if (world.provider.isSurfaceWorld()) {
			newTemperatureLevel += TIME_TARGET_MODIFIER * timeNorm * (Math
					.max(1.0F, extremityModifier * EXTREMITY_MULTIPLIER));
		}

		debugger.end(newTemperatureLevel);

		return new Temperature(newTemperatureLevel);
	}

	public Temperature modifyTarget(World world, BlockPos position,
			Temperature temperature) {
		Biome biome = world.getBiomeGenForCoords(position);
		long worldTime = world.getWorldTime();

		float extremityModifier = BiomeUtils.getBiomeTempExtremity(biome);
		// Reaches the highest point during the middle of the day and at
		// midnight. Normalized to be between -1 and 1
		float timeNorm = (-Math.abs(((worldTime + 6000) % 24000.0F) - 12000.0F)
				+ 6000.0F) / 6000.0F;

		int temperatureLevel = temperature.getRawValue();
		int newTemperatureLevel = temperatureLevel;

		if (world.provider.isSurfaceWorld()) {
			newTemperatureLevel += TIME_TARGET_MODIFIER * timeNorm * (Math
					.max(1.0F, extremityModifier * EXTREMITY_MULTIPLIER));
		}

		return new Temperature(newTemperatureLevel);
	}
}
