/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.temperature.modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.Season.SubSeason;
import toughasnails.api.season.SeasonHelper;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.config.GameplayOption;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureTrend;

public class SeasonModifier extends TemperatureModifier {

	public final int EARLY_AUTUMN_MODIFIER;
	public final int MID_AUTUMN_MODIFIER;
	public final int LATE_AUTUMN_MODIFIER;
	public final int EARLY_WINTER_MODIFIER;
	public final int MID_WINTER_MODIFIER;
	public final int LATE_WINTER_MODIFIER;
	public final int EARLY_SPRING_MODIFIER;
	public final int MID_SPRING_MODIFIER;
	public final int LATE_SPRING_MODIFIER;
	public final int EARLY_SUMMER_MODIFIER;
	public final int MID_SUMMER_MODIFIER;
	public final int LATE_SUMMER_MODIFIER;

	public SeasonModifier(TemperatureDebugger debugger) {
		super(debugger);
		this.EARLY_AUTUMN_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.EARLY_AUTUMN_MODIFIER);
		this.MID_AUTUMN_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.MID_AUTUMN_MODIFIER);
		this.LATE_AUTUMN_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.LATE_AUTUMN_MODIFIER);

		this.EARLY_WINTER_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.EARLY_WINTER_MODIFIER);
		this.MID_WINTER_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.MID_WINTER_MODIFIER);
		this.LATE_WINTER_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.LATE_WINTER_MODIFIER);

		this.EARLY_SPRING_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.EARLY_SPRING_MODIFIER);
		this.MID_SPRING_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.MID_SPRING_MODIFIER);
		this.LATE_SPRING_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.LATE_SPRING_MODIFIER);

		this.EARLY_SUMMER_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.EARLY_SUMMER_MODIFIER);
		this.MID_SUMMER_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.MID_SUMMER_MODIFIER);
		this.LATE_SUMMER_MODIFIER = SyncedConfig
				.getIntegerValue(GameplayOption.LATE_SUMMER_MODIFIER);
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
		SubSeason season = SeasonHelper.getSeasonData(world).getSubSeason();

		if (!(SyncedConfig.getBooleanValue(GameplayOption.ENABLE_SEASONS))) {
			season = SubSeason.MID_SUMMER;
		}

		debugger.start(Modifier.SEASON_TARGET, temperatureLevel);

		if (world.provider.isSurfaceWorld()) {
			switch (season) {
			case EARLY_AUTUMN:
				temperatureLevel += this.EARLY_AUTUMN_MODIFIER;
				break;
			case MID_AUTUMN:
				temperatureLevel += this.MID_AUTUMN_MODIFIER;
				break;
			case LATE_AUTUMN:
				temperatureLevel += this.LATE_AUTUMN_MODIFIER;
				break;
			case EARLY_WINTER:
				temperatureLevel += this.EARLY_WINTER_MODIFIER;
				break;
			case MID_WINTER:
				temperatureLevel += this.MID_WINTER_MODIFIER;
				break;
			case LATE_WINTER:
				temperatureLevel += this.LATE_WINTER_MODIFIER;
				break;
			case EARLY_SPRING:
				temperatureLevel += this.EARLY_SPRING_MODIFIER;
				break;
			case MID_SPRING:
				temperatureLevel += this.MID_SPRING_MODIFIER;
				break;
			case LATE_SPRING:
				temperatureLevel += this.LATE_SPRING_MODIFIER;
				break;
			case EARLY_SUMMER:
				temperatureLevel += this.EARLY_SUMMER_MODIFIER;
				break;
			case MID_SUMMER:
				temperatureLevel += this.MID_SUMMER_MODIFIER;
				break;
			case LATE_SUMMER:
				temperatureLevel += this.LATE_SUMMER_MODIFIER;
				break;
			default:
				break;
			}
		}
		debugger.end(temperatureLevel);

		return new Temperature(temperatureLevel);
	}

	public Temperature modifyTarget(World world, BlockPos position,
			Temperature temperature) {
		int temperatureLevel = temperature.getRawValue();
		SubSeason season = SeasonHelper.getSeasonData(world).getSubSeason();

		if (!(SyncedConfig.getBooleanValue(GameplayOption.ENABLE_SEASONS))) {
			season = SubSeason.MID_SUMMER;
		}

		if (world.provider.isSurfaceWorld()) {
			switch (season) {
			case EARLY_AUTUMN:
				temperatureLevel += this.EARLY_AUTUMN_MODIFIER;
				break;
			case MID_AUTUMN:
				temperatureLevel += this.MID_AUTUMN_MODIFIER;
				break;
			case LATE_AUTUMN:
				temperatureLevel += this.LATE_AUTUMN_MODIFIER;
				break;
			case EARLY_WINTER:
				temperatureLevel += this.EARLY_WINTER_MODIFIER;
				break;
			case MID_WINTER:
				temperatureLevel += this.MID_WINTER_MODIFIER;
				break;
			case LATE_WINTER:
				temperatureLevel += this.LATE_WINTER_MODIFIER;
				break;
			case EARLY_SPRING:
				temperatureLevel += this.EARLY_SPRING_MODIFIER;
				break;
			case MID_SPRING:
				temperatureLevel += this.MID_SPRING_MODIFIER;
				break;
			case LATE_SPRING:
				temperatureLevel += this.LATE_SPRING_MODIFIER;
				break;
			case EARLY_SUMMER:
				temperatureLevel += this.EARLY_SUMMER_MODIFIER;
				break;
			case MID_SUMMER:
				temperatureLevel += this.MID_SUMMER_MODIFIER;
				break;
			case LATE_SUMMER:
				temperatureLevel += this.LATE_SUMMER_MODIFIER;
				break;
			default:
				break;
			}
		}

		return new Temperature(temperatureLevel);
	}
}
