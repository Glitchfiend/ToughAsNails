package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import toughasnails.api.temperature.Temperature;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureTrend;

public class PlayerStateModifier extends TemperatureModifier {
	public static final int SPRINTING_RATE_MODIFIER = 200;
	public static final int SPRINTING_TARGET_MODIFIER = 3;

	public PlayerStateModifier(TemperatureDebugger debugger) {
		super(debugger);
	}

	@Override
	public int modifyChangeRate(World world, EntityPlayer player,
			int changeRate, TemperatureTrend trend) {
		int newChangeRate = changeRate;
		int sprintingRateModifier = SPRINTING_RATE_MODIFIER;

		switch (trend) {
		case INCREASING:
			sprintingRateModifier *= -1;
			break;
		case STILL:
			sprintingRateModifier = 0;
			break;
		default:
			sprintingRateModifier = 0;
			break;
		}

		debugger.start(Modifier.SPRINTING_RATE, newChangeRate);

		if (player.isSprinting()) {
			newChangeRate += sprintingRateModifier;
		}

		debugger.end(newChangeRate);
		debugger.start(Modifier.HEALTH_RATE, newChangeRate);

		newChangeRate -= (1.0 - (player.getHealth() / player.getMaxHealth()))
				* 200;

		debugger.end(newChangeRate);

		return newChangeRate;
	}

	@Override
	public Temperature modifyTarget(World world, EntityPlayer player,
			Temperature temperature) {
		int temperatureLevel = temperature.getRawValue();
		int newTemperatureLevel = temperatureLevel;

		debugger.start(Modifier.SPRINTING_TARGET, newTemperatureLevel);

		if (player.isSprinting()) {
			newTemperatureLevel += SPRINTING_TARGET_MODIFIER;
		}

		debugger.end(newTemperatureLevel);

		return new Temperature(newTemperatureLevel);
	}
}
