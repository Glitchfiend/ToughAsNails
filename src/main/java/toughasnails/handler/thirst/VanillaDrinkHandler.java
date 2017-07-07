/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.thirst;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.thirst.ThirstHandler;

public class VanillaDrinkHandler {
	@SubscribeEvent
	public void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack stack = player.getHeldItem(player.getActiveHand());
			ThirstHandler thirstHandler = (ThirstHandler) ThirstHelper
					.getThirstData(player);

			if (thirstHandler.isThirsty()) {
				ResourceLocation resLoc = ForgeRegistries.ITEMS
						.getKey(stack.getItem());
				String itemName = resLoc.toString();

				List<String> drinks = SyncedConfig
						.getListValue(GameplayOption.DRINKS);

				for (String drinkEntry : drinks) {
					String[] drinkData = drinkEntry.split(";");
					if (drinkData.length == 3) {
						if (itemName.equals(drinkData[0])) {
							int thirstLevel = 0;
							float hydration = 0f;
							try {
								thirstLevel = Integer.parseInt(drinkData[1]);
								hydration = Float.parseFloat(drinkData[2]);
								thirstHandler.addStats(thirstLevel, hydration);
							} catch (NumberFormatException e) {
								System.out
										.println("Tried to drink misconfigured "
												+ itemName);
							}
						}
					}
				}
			}
		}
	}
}
