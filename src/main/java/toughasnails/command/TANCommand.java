package toughasnails.command;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import toughasnails.api.TANCapabilities;
import toughasnails.api.TANPotions;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.season.Season.SubSeason;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.handler.health.MaxHealthHandler;
import toughasnails.handler.season.SeasonHandler;
import toughasnails.season.SeasonSavedData;
import toughasnails.season.SeasonTime;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureHandler;
import toughasnails.thirst.ThirstHandler;

public class TANCommand extends CommandBase {
	@Override
	public String getCommandName() {
		return "toughasnails";
	}

	@Override
	public List<String> getCommandAliases() {
		return Lists.newArrayList("tan");
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.toughasnails.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException("commands.toughasnails.usage");
		} else if ("tempinfo".equals(args[0])) {
			displayTemperatureInfo(sender, args);
		} else if ("tempat".equals(args[0])) {
			if (args.length < 5 || args.length > 6) {
				throw new WrongUsageException("commands.toughasnails.usage");
			} else {
				retrieveTemperatureAt(sender, args);
			}
		} else if ("settemp".equals(args[0])) {
			setTemperature(sender, args);
		} else if ("setseason".equals(args[0])) {
			if (args.length < 3 || args.length > 4) {
				throw new WrongUsageException("commands.toughasnails.usage");
			} else {
				setSeason(sender, args);
			}
		} else if ("setthirst".equals(args[0])) {
			if (args.length != 3) {
				throw new WrongUsageException("commands.toughasnails.usage");
			} else {
				setThirst(sender, args);
			}
		} else if ("sethealth".equals(args[0])) {
			if (args.length != 3) {
				throw new WrongUsageException("commands.toughasnails.usage");
			} else {
				setHealth(sender, args);
			}
		}
	}

	private void displayTemperatureInfo(ICommandSender sender, String[] args)
			throws CommandException {
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		TemperatureHandler temperatureStats = (TemperatureHandler) player
				.getCapability(TANCapabilities.TEMPERATURE, null);
		TemperatureDebugger debugger = temperatureStats.debugger;

		if (SyncedConfig.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE)) {
			debugger.setGuiVisible(!debugger.isGuiVisible(), player);
		} else {
			sender.addChatMessage(new TextComponentTranslation(
					"commands.toughasnails.settemp.disabled"));
		}
	}

	// tan tempat <world> <x> <y> <z>
	private void retrieveTemperatureAt(ICommandSender sender, String[] args)
			throws CommandException {
		int dimensionID = 0;
		int x = 0;
		int y = 0;
		int z = 0;
		boolean printOutput = true;
		try {
			dimensionID = Integer.parseInt(args[1]);
			x = Integer.parseInt(args[2]);
			y = Integer.parseInt(args[3]);
			z = Integer.parseInt(args[4]);
			if (args.length >= 6) {
				printOutput = Boolean.parseBoolean(args[5]);
			}
		} catch (NumberFormatException e) {
			throw new WrongUsageException("commands.toughasnails.usage");
		}

		if (SyncedConfig.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE)) {
			World world = null;
			WorldServer[] worldServers = FMLCommonHandler.instance()
					.getMinecraftServerInstance().worldServers;
			WorldServer candidate = FMLCommonHandler.instance()
					.getMinecraftServerInstance()
					.worldServerForDimension(dimensionID);
			if (candidate == null) {
				throw new WrongUsageException("commands.toughasnails.usage");
			}

			for (int i = 0; i < worldServers.length; i++) {
				WorldServer target = worldServers[i];
				if (candidate.equals(target)) {
					world = target;
					break;
				}
			}

			if (world == null) {
				throw new WrongUsageException("commands.toughasnails.usage");
			}
			BlockPos position = new BlockPos(x, y, z);

			int finalTemperature = TemperatureHandler
					.getTargetTemperatureAt(world, position);
			if (printOutput) {
				sender.addChatMessage(new TextComponentTranslation(
						"commands.toughasnails.tempat.success", dimensionID, x,
						y, z, finalTemperature));
			}
			sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT,
					finalTemperature);
		} else {
			sender.addChatMessage(new TextComponentTranslation(
					"commands.toughasnails.tempat.disabled"));
		}
	}

	private void setTemperature(ICommandSender sender, String[] args)
			throws CommandException {
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		TemperatureHandler temperatureStats = (TemperatureHandler) player
				.getCapability(TANCapabilities.TEMPERATURE, null);
		int newTemp = parseInt(args[1], 0, TemperatureScale.getScaleTotal());
		// Temperature playerTemp = temperatureStats.getTemperature();

		if (SyncedConfig.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE)) {
			// Remove any existing potion effects for hypo/hyperthermia
			player.removePotionEffect(TANPotions.hypothermia);
			player.removePotionEffect(TANPotions.hyperthermia);

			// Reset the change timer to 0
			temperatureStats.setChangeTime(0);
			// Set to the new temperature
			temperatureStats.setTemperature(new Temperature(newTemp));

			sender.addChatMessage(new TextComponentTranslation(
					"commands.toughasnails.settemp.success", newTemp));
		} else {
			sender.addChatMessage(new TextComponentTranslation(
					"commands.toughasnails.settemp.disabled"));
		}
	}

	private void setSeason(ICommandSender sender, String[] args)
			throws CommandException {
		int dimensionID = 0;
		boolean printOutput = true;
		try {
			dimensionID = Integer.parseInt(args[2]);
			if (args.length >= 4) {
				printOutput = Boolean.parseBoolean(args[3]);
			}
		} catch (NumberFormatException e) {
			throw new WrongUsageException("commands.toughasnails.usage");
		}
		SubSeason newSeason = null;

		for (SubSeason season : SubSeason.values()) {
			if (season.toString().toLowerCase().equals(args[1].toLowerCase())) {
				newSeason = season;
				break;
			}
		}

		if (SyncedConfig.getBooleanValue(GameplayOption.ENABLE_SEASONS)) {
			if (newSeason != null) {
				World world = null;
				WorldServer[] worldServers = FMLCommonHandler.instance()
						.getMinecraftServerInstance().worldServers;
				WorldServer candidate = FMLCommonHandler.instance()
						.getMinecraftServerInstance()
						.worldServerForDimension(dimensionID);
				if (candidate == null) {
					throw new WrongUsageException(
							"commands.toughasnails.usage");
				}

				for (int i = 0; i < worldServers.length; i++) {
					WorldServer target = worldServers[i];
					if (candidate.equals(target)) {
						world = target;
						break;
					}
				}

				if (world == null) {
					throw new WrongUsageException(
							"commands.toughasnails.usage");
				}

				SeasonSavedData seasonData = SeasonHandler
						.getSeasonSavedData(world);
				seasonData.seasonCycleTicks = SeasonTime.DAY_TICKS
						* SeasonTime.SUB_SEASON_DURATION * newSeason.ordinal();
				seasonData.markDirty();
				SeasonHandler.sendSeasonUpdate(world);
				if (printOutput) {
					sender.addChatMessage(new TextComponentTranslation(
							"commands.toughasnails.setseason.success",
							args[1]));
				}
			} else {
				sender.addChatMessage(new TextComponentTranslation(
						"commands.toughasnails.setseason.fail", args[1]));
			}
		} else {
			sender.addChatMessage(new TextComponentTranslation(
					"commands.toughasnails.setseason.disabled"));
		}
	}

	private void setHealth(ICommandSender sender, String[] args)
			throws CommandException {
		String playerName = args[1];
		EntityPlayerMP player = FMLCommonHandler.instance()
				.getMinecraftServerInstance().getPlayerList()
				.getPlayerByUsername(playerName);
		if (player == null) {
			throw new WrongUsageException("commands.toughasnails.usage");
		}

		int newHealth = 0;
		try {
			newHealth = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			throw new WrongUsageException("commands.toughasnails.usage");
		}

		// If health is enabled
		if (SyncedConfig.getBooleanValue(
				GameplayOption.ENABLE_LOWERED_STARTING_HEALTH)) {

			// Set the new health
			MaxHealthHandler.overrideMaximumHealth(player.getUniqueID(),
					newHealth);

			sender.addChatMessage(new TextComponentTranslation(
					"commands.toughasnails.sethealth.success", newHealth));
		} else {
			sender.addChatMessage(new TextComponentTranslation(
					"commands.toughasnails.sethealth.disabled"));
		}
	}

	private void setThirst(ICommandSender sender, String[] args)
			throws CommandException {
		String playerName = args[1];
		EntityPlayerMP player = FMLCommonHandler.instance()
				.getMinecraftServerInstance().getPlayerList()
				.getPlayerByUsername(playerName);
		if (player == null) {
			throw new WrongUsageException("commands.toughasnails.usage");
		}

		int thirstLevel = 0;
		try {
			thirstLevel = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			throw new WrongUsageException("commands.toughasnails.usage");
		}

		// If thirst is enabled
		if (SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST)) {

			// Remove any existing potion effects for thirst
			player.removePotionEffect(TANPotions.thirst);

			// Set the new thirst
			ThirstHandler thirstHandler = (ThirstHandler) ThirstHelper
					.getThirstData(player);
			thirstHandler.setThirst(thirstLevel);

			sender.addChatMessage(new TextComponentTranslation(
					"commands.toughasnails.setthirst.success", thirstLevel));
		} else {
			sender.addChatMessage(new TextComponentTranslation(
					"commands.toughasnails.setthirst.disabled"));
		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server,
			ICommandSender sender, String[] args, BlockPos pos) {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, "settemp", "tempinfo",
					"setseason", "sethealth", "setthirst");
		}

		return null;
	}
}
