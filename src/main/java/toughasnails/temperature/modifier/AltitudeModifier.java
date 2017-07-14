package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.temperature.Temperature;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureTrend;

public class AltitudeModifier extends TemperatureModifier {
	public final int ALTITUDE_TARGET_MODIFIER;

	public AltitudeModifier(TemperatureDebugger debugger) {
		super(debugger);
		this.ALTITUDE_TARGET_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.ALTITUDE_TEMP_MODIFIER);
	}

	@Override
	public int modifyChangeRate(World world, EntityPlayer player,
			int changeRate, TemperatureTrend trend) {
		return changeRate;
	}

	@Override
	public Temperature modifyTarget(World world, EntityPlayer player,
			Temperature temperature) {
		int temperatureLevel = temperature.getRawValue();
		int newTemperatureLevel = temperatureLevel;

		debugger.start(Modifier.ALTITUDE_TARGET, newTemperatureLevel);

		if (world.provider.isSurfaceWorld()) {
			newTemperatureLevel -= MathHelper.abs_int(MathHelper.floor_double(
					((64 - player.posY) / 64) * ALTITUDE_TARGET_MODIFIER) + 1);
		}

		debugger.end(newTemperatureLevel);

		return new Temperature(newTemperatureLevel);
	}

	public Temperature modifyTarget(World world, BlockPos position,
			Temperature temperature) {
		int temperatureLevel = temperature.getRawValue();
		int newTemperatureLevel = temperatureLevel;

		if (world.provider.isSurfaceWorld()) {
			newTemperatureLevel -= MathHelper.abs_int(MathHelper.floor_double(
					((64 - position.getY()) / 64) * ALTITUDE_TARGET_MODIFIER)
					+ 1);
		}

		return new Temperature(newTemperatureLevel);
	}
}
