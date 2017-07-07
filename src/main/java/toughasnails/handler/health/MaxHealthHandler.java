/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.health;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.HealthHelper;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;

public class MaxHealthHandler {
	// TODO: If the health config option is changed and the current health is
	// lower increase it to that new default
	@SubscribeEvent
	public void onPlayerLogin(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		World world = player.worldObj;

		if (!world.isRemote) {
			updateStartingHealthModifier(world.getDifficulty(), player);
		}
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		IAttributeInstance oldMaxHealthInstance = event.getOriginal()
				.getAttributeMap()
				.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
		AttributeModifier modifier = oldMaxHealthInstance
				.getModifier(HealthHelper.LIFEBLOOD_HEALTH_MODIFIER_ID);

		// Copy the lifeblood modifier from the 'old' player
		if (SyncedConfig
				.getBooleanValue(GameplayOption.ENABLE_LOWERED_STARTING_HEALTH)
				&& modifier != null) {
			Multimap<String, AttributeModifier> multimap = HashMultimap
					.<String, AttributeModifier> create();
			multimap.put(SharedMonsterAttributes.MAX_HEALTH
					.getAttributeUnlocalizedName(), modifier);
			event.getEntityPlayer().getAttributeMap()
					.applyAttributeModifiers(multimap);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		Minecraft minecraft = Minecraft.getMinecraft();
		IntegratedServer integratedServer = minecraft.getIntegratedServer();

		if (SyncedConfig
				.getBooleanValue(GameplayOption.ENABLE_LOWERED_STARTING_HEALTH)
				&& event.phase == Phase.END && integratedServer != null) {
			boolean gamePaused = Minecraft.getMinecraft()
					.getConnection() != null && minecraft.isGamePaused();

			if (!gamePaused && minecraft.theWorld != null) {
				WorldInfo serverWorldInfo = integratedServer.worldServers[0]
						.getWorldInfo();
				WorldInfo localWorldInfo = minecraft.theWorld.getWorldInfo();

				// This is checked before the difficulty is actually changed to
				// make the two match in IntegratedServer's tick()
				if (localWorldInfo.getDifficulty() != serverWorldInfo
						.getDifficulty()) {
					List<EntityPlayerMP> players = integratedServer
							.getPlayerList().getPlayerList();

					// Update the modifiers of all the connected players
					for (EntityPlayerMP player : players) {
						updateStartingHealthModifier(
								localWorldInfo.getDifficulty(), player);
					}
				}
			}
		}
	}

	private static Map<UUID, Integer> maxHealth = new HashMap<UUID, Integer>();

	public static void overrideMaximumHealth(UUID player,
			int newMaximumHealth) {
		if (newMaximumHealth > 0 && newMaximumHealth <= 20) {
			maxHealth.put(player, newMaximumHealth);
			File f = new File("player_health_overrides.txt");
			List<String> lines = new ArrayList<String>();
			try {
				if (!f.exists()) {
					f.createNewFile();
				}

				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				while ((line = br.readLine()) != null) {
					String[] split = line.split("\\|");
					String idString = split[0];
					if (!player.toString().equals(idString)) {
						lines.add(line);
					}
				}
				br.close();

				f.delete();
				f.createNewFile();
				PrintWriter writer = new PrintWriter(
						new FileWriter("player_health_overrides.txt"));
				for (String outLine : lines) {
					writer.println(outLine);
				}
				writer.println(player.toString() + "|" + newMaximumHealth);
				writer.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateStartingHealthModifier(EnumDifficulty difficulty,
			EntityPlayer player) {
		UUID id = player.getUniqueID();
		IAttributeInstance maxHealthInstance = player.getAttributeMap()
				.getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
		AttributeModifier modifier = maxHealthInstance
				.getModifier(HealthHelper.STARTING_HEALTH_MODIFIER_ID);

		// Don't update if the lowered starting health config option is disabled
		if (!SyncedConfig.getBooleanValue(
				GameplayOption.ENABLE_LOWERED_STARTING_HEALTH)) {
			if (modifier != null) {
				maxHealthInstance.removeModifier(
						HealthHelper.STARTING_HEALTH_MODIFIER_ID);
			}

			return;
		}

		double difficultyHealthDecrement = 0;
		boolean initialized = false;
		if (!maxHealth.containsKey(id)) {
			File f = new File("player_health_overrides.txt");
			try {
				if (!f.exists()) {
					f.createNewFile();
				}

				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				while ((line = br.readLine()) != null) {
					String split[] = line.split("\\|");
					String idString = split[0];
					String maxHealthString = split[1];
					if (id.toString().equals(idString)) {
						maxHealth.put(id, Integer.parseInt(maxHealthString));
						initialized = true;
						break;
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			difficultyHealthDecrement = -(20 - maxHealth.get(id));
			initialized = true;
		}

		if (!initialized) {
			switch (difficulty) {
			case EASY:
				difficultyHealthDecrement = -6.0D;
				maxHealth.put(id, 14);
				break;

			case NORMAL:
				difficultyHealthDecrement = -10.0D;
				maxHealth.put(id, 10);
				break;

			case HARD:
				difficultyHealthDecrement = -14.0D;
				maxHealth.put(id, 6);
				break;

			default:
				difficultyHealthDecrement = -0.0D;
				maxHealth.put(id, 20);
				break;
			}
		}

		double lifebloodHearts = HealthHelper.getLifebloodHearts(player) * 2;
		double overallHealthDecrement = difficultyHealthDecrement
				+ lifebloodHearts;

		// Ensure that the total hearts is never above 20 when the difficulty is
		// changed
		if (overallHealthDecrement > 0.0D) {
			difficultyHealthDecrement -= overallHealthDecrement;
		}

		// If the player doesn't have a modifier for a lowered starting health,
		// add one
		// Or alternatively, if the player already has the attribute, update it
		// only if it is less than the current difficulty
		// When the difficulty is changed locally in the options menu, it should
		// always change (forceUpdate)
		if (modifier == null
				|| modifier.getAmount() != difficultyHealthDecrement) {
			Multimap<String, AttributeModifier> multimap = HashMultimap
					.<String, AttributeModifier> create();
			modifier = new AttributeModifier(
					HealthHelper.STARTING_HEALTH_MODIFIER_ID,
					"Starting Health Modifier", difficultyHealthDecrement, 0);
			multimap.put(SharedMonsterAttributes.MAX_HEALTH
					.getAttributeUnlocalizedName(), modifier);
			player.getAttributeMap().applyAttributeModifiers(multimap);

			if (player.getHealth() > player.getMaxHealth()) {
				player.setHealth(player.getMaxHealth());
			}
		}
	}
}
