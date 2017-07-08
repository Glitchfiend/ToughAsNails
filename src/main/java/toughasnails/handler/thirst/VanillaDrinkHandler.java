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
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import toughasnails.api.TANPotions;
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
				// For some reason the stack size can be zero for water bottles,
				// which breaks everything. As a workaround, we temporarily set
				// it to 1
				boolean zeroStack = false;
				if (stack.stackSize <= 0) {
					stack.stackSize = 1;
					zeroStack = true;
				}
				if (stack.getItem().equals(Items.POTIONITEM)) {
					if (PotionUtils.getFullEffectsFromItem(stack).isEmpty()) {
						thirstHandler.addStats(7, 0.5F);
					} else {
						// Still fill thirst for other potions, but less than
						// water
						thirstHandler.addStats(4, 0.3F);
					}
				}
				if (zeroStack) {
					stack.stackSize = 0;
				}

				// Check for any additional configured drinks.
				ResourceLocation resLoc = ForgeRegistries.ITEMS
						.getKey(stack.getItem());
				String itemName = resLoc.toString();

				List<String> drinks = SyncedConfig
						.getListValue(GameplayOption.DRINKS);

				for (String drinkEntry : drinks) {
					String[] drinkData = drinkEntry.split(";");
					if (drinkData.length == 5) {
						String stackDamageString = "" + stack.getItemDamage();
						String drinkDamage = drinkData[1];
						boolean isProperDamage = (drinkDamage.equals("*")
								|| drinkDamage.equals(stackDamageString));
						if (itemName.equals(drinkData[0]) && isProperDamage) {
							int thirstLevel = 0;
							float hydration = 0f;
							float poisonChance = 0f;
							try {
								thirstLevel = Integer.parseInt(drinkData[2]);
								hydration = Float.parseFloat(drinkData[3]);
								poisonChance = Float.parseFloat(drinkData[4]);
								thirstHandler.addStats(thirstLevel, hydration);

								// Roll for poison
								if (player.worldObj.rand
										.nextFloat() < poisonChance
										&& SyncedConfig.getBooleanValue(
												GameplayOption.ENABLE_THIRST)) {
									player.addPotionEffect(new PotionEffect(
											TANPotions.thirst, 600));
								}
							} catch (NumberFormatException e) {
								System.out
										.println("Tried to drink misconfigured "
												+ itemName);
							}
							break;
						}
					}
				}
			}
		}
	}
}
