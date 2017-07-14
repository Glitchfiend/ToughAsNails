/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.season;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.IDecayableCrop;
import toughasnails.api.season.IHibernatingCrop;
import toughasnails.api.season.ISeasonData;
import toughasnails.api.season.Season;
import toughasnails.api.season.Season.SubSeason;
import toughasnails.api.season.SeasonHelper;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.config.CropGrowConfigEntry;
import toughasnails.config.GameplayConfigurationHandler;
import toughasnails.handler.PacketHandler;
import toughasnails.network.message.MessageSyncSeasonCycle;
import toughasnails.season.SeasonSavedData;
import toughasnails.season.SeasonTime;
import toughasnails.temperature.TemperatureHandler;

public class SeasonHandler {

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		World world = event.world;

		if (event.phase == TickEvent.Phase.END && !world.isRemote
				&& world.provider.getDimension() == 0 && SyncedConfig
						.getBooleanValue(GameplayOption.ENABLE_SEASONS)) {
			SeasonSavedData savedData = getSeasonSavedData(world);

			if (savedData.seasonCycleTicks++ > SeasonTime.TOTAL_CYCLE_TICKS) {
				savedData.seasonCycleTicks = 0;
			}

			if (savedData.seasonCycleTicks % 20 == 0) {
				sendSeasonUpdate(world);
			}

			savedData.markDirty();
		}
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		World world = player.worldObj;

		sendSeasonUpdate(world);
	}

	private SubSeason lastSeason = null;
	public static int clientSeasonCycleTicks = 0;

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		// Only do this when in the world
		if (Minecraft.getMinecraft().thePlayer == null)
			return;

		int dimension = Minecraft.getMinecraft().thePlayer.dimension;

		if (event.phase == TickEvent.Phase.END && dimension == 0 && SyncedConfig
				.getBooleanValue(GameplayOption.ENABLE_SEASONS)) {
			// Keep ticking as we're synchronized with the server only every
			// second
			if (clientSeasonCycleTicks++ > SeasonTime.TOTAL_CYCLE_TICKS) {
				clientSeasonCycleTicks = 0;
			}

			SeasonTime calendar = new SeasonTime(clientSeasonCycleTicks);

			if (calendar.getSubSeason() != lastSeason) {
				Minecraft.getMinecraft().renderGlobal.loadRenderers();
				lastSeason = calendar.getSubSeason();
			}
		}
	}

	public static void sendSeasonUpdate(World world) {
		if (!world.isRemote && SyncedConfig
				.getBooleanValue(GameplayOption.ENABLE_SEASONS)) {
			SeasonSavedData savedData = getSeasonSavedData(world);
			PacketHandler.instance.sendToAll(
					new MessageSyncSeasonCycle(savedData.seasonCycleTicks));
		}
	}

	public static SeasonSavedData getSeasonSavedData(World world) {
		MapStorage mapStorage = world.getPerWorldStorage();
		SeasonSavedData savedData = (SeasonSavedData) mapStorage.getOrLoadData(
				SeasonSavedData.class, SeasonSavedData.DATA_IDENTIFIER);

		// If the saved data file hasn't been created before, create it
		if (savedData == null) {
			savedData = new SeasonSavedData(SeasonSavedData.DATA_IDENTIFIER);
			mapStorage.setData(SeasonSavedData.DATA_IDENTIFIER, savedData);
			savedData.markDirty(); // Mark for saving
		}

		return savedData;
	}

	/*
	 * Check for hibernating crops attempting to grow in the cold or heat, and
	 * block them.
	 */
	@SubscribeEvent
	public void onCropGrowPre(BlockEvent.CropGrowEvent.Pre event) {
		Block block = event.getState().getBlock();
		String blockName = block.getRegistryName().toString();
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		boolean temperatureWithering = SyncedConfig
				.getBooleanValue(GameplayOption.TEMPERATURE_WITHERING);
		if (temperatureWithering) {
			int minHibernate = 0;
			int minOptimal = 0;
			int maxOptimal = 0;
			int maxHibernate = 0;
			float nonOptimalChance = 0;
			// Assign crop hibernation details from config file.
			if (GameplayConfigurationHandler.EXTERNAL_HIBERNATING_CROPS
					.containsKey(blockName)) {
				CropGrowConfigEntry cropData = GameplayConfigurationHandler.EXTERNAL_HIBERNATING_CROPS
						.get(blockName);
				minHibernate = cropData.getMinLiving();
				minOptimal = cropData.getMinOptimal();
				maxOptimal = cropData.getMaxOptimal();
				maxHibernate = cropData.getMaxLiving();
				nonOptimalChance = cropData.getNonOptimalChance();
				System.out.println(blockName + " using spec " + minHibernate
						+ ", " + minOptimal + ", " + maxOptimal + ", "
						+ maxHibernate + ", " + nonOptimalChance);

				// Otherwise, assign defaults.
			} else if (block instanceof IHibernatingCrop
					&& ((IHibernatingCrop) block).shouldHibernate()) {
				minHibernate = 5;
				minOptimal = 10;
				maxOptimal = 15;
				maxHibernate = 20;
				nonOptimalChance = 0.5f;
				System.out.println(blockName + " using default.");
			} else {
				System.out.println(blockName + " is not a hibernater.");
				return;
			}

			// Alive but not optimal, slow growth
			int targetTemperature = TemperatureHandler
					.getTargetTemperatureAt(world, pos);
			if ((targetTemperature > maxOptimal
					&& targetTemperature <= maxHibernate)
					|| (targetTemperature < minOptimal
							&& targetTemperature >= minHibernate)
							&& (Math.random() < nonOptimalChance)) {
				System.out.println("non-optimal blocking");
				event.setResult(Result.DENY);
			} else if (targetTemperature < minHibernate
					|| targetTemperature > maxHibernate) {
				System.out.println("temp exceeded blocking");
				event.setResult(Result.DENY);
			}
		} else {
			Season season = SeasonHelper.getSeasonData(world).getSubSeason()
					.getSeason();
			if (season == Season.WINTER
					&& (block instanceof IHibernatingCrop
							&& ((IHibernatingCrop) block).shouldHibernate())
					&& !TemperatureHelper.isPosClimatisedForTemp(world, pos,
							new Temperature(1))
					&& SyncedConfig
							.getBooleanValue(GameplayOption.ENABLE_SEASONS)) {
				event.setResult(Event.Result.DENY);
			}
		}
	}

	/*
	 * Detect crop grow events and check for the optimal growth condition.
	 */
	@SubscribeEvent
	public void onCropGrowEvent(BlockEvent.CropGrowEvent event) {
		if (!event.getWorld().isRemote) {
			// This type of growth only applies when temperature decay is
			// enabled.
			Block block = event.getState().getBlock();
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			String blockName = block.getRegistryName().toString();
			boolean temperatureWithering = SyncedConfig
					.getBooleanValue(GameplayOption.TEMPERATURE_WITHERING);
			if (temperatureWithering) {
				int minLiving = 0;
				int minOptimal = 0;
				int maxOptimal = 0;
				int maxLiving = 0;
				float nonOptimalChance = 0;
				// Assign crop life details from config file.
				if (GameplayConfigurationHandler.EXTERNAL_DECAYING_CROPS
						.containsKey(blockName)) {
					CropGrowConfigEntry cropData = GameplayConfigurationHandler.EXTERNAL_DECAYING_CROPS
							.get(blockName);
					minLiving = cropData.getMinLiving();
					minOptimal = cropData.getMinOptimal();
					maxOptimal = cropData.getMaxOptimal();
					maxLiving = cropData.getMaxLiving();
					nonOptimalChance = cropData.getNonOptimalChance();

					// Otherwise, assign defaults.
				} else if (block instanceof IDecayableCrop
						&& ((IDecayableCrop) block).shouldDecay()) {
					minLiving = 5;
					minOptimal = 10;
					maxOptimal = 15;
					maxLiving = 20;
					nonOptimalChance = 0.5f;
				} else {
					return;
				}

				// Alive but not optimal, slow growth
				int targetTemperature = TemperatureHandler
						.getTargetTemperatureAt(world, pos);
				if ((targetTemperature > maxOptimal
						&& targetTemperature <= maxLiving)
						|| (targetTemperature < minOptimal
								&& targetTemperature >= minLiving)
								&& (Math.random() < nonOptimalChance)) {
					event.setResult(Result.DENY);
				}
			}
		}
	}

	//
	// Used to implement getSeasonData in the API
	//
	public static ISeasonData getServerSeasonData(World world) {
		SeasonSavedData savedData = getSeasonSavedData(world);
		return new SeasonTime(savedData.seasonCycleTicks);
	}

	public static ISeasonData getClientSeasonData() {
		return new SeasonTime(clientSeasonCycleTicks);
	}
}
