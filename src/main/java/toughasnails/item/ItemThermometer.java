/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureHandler;

public class ItemThermometer extends Item {
	public ItemThermometer() {
		this.addPropertyOverride(new ResourceLocation("temperature"),
				new IItemPropertyGetter() {
					@SideOnly(Side.CLIENT)
					double field_185088_a;
					@SideOnly(Side.CLIENT)
					double field_185089_b;
					@SideOnly(Side.CLIENT)
					int ticks;

					@Override
					@SideOnly(Side.CLIENT)
					public float apply(ItemStack stack, World world,
							EntityLivingBase entity) {
						boolean overrideThermometerLimits = SyncedConfig
								.getBooleanValue(
										GameplayOption.OVERRIDE_THERMOMETER_LIMITS);
						int lowerBound = SyncedConfig.getIntegerValue(
								GameplayOption.THERMOMETER_LOWER_BOUND);
						int upperBound = SyncedConfig.getIntegerValue(
								GameplayOption.THERMOMETER_UPPER_BOUND);

						if (entity == null
								|| !(entity instanceof EntityPlayer)) {
							Entity frame = stack.getItemFrame();
							if (frame != null) {
								BlockPos framePosition = frame.getPosition();
								World frameWorld = frame.getEntityWorld();
								int finalTemperature = TemperatureHandler
										.getTargetTemperatureAt(frameWorld,
												framePosition);

								if (overrideThermometerLimits) {
									float clampedTemp = (float) MathHelper
											.clamp_double(finalTemperature,
													lowerBound, upperBound);
									float shiftedTemp = (clampedTemp
											- lowerBound);
									float needlePosition = shiftedTemp
											/ ((float) (upperBound
													- lowerBound));
									return needlePosition;
								} else {
									return (float) MathHelper
											.clamp_double(finalTemperature, 0,
													TemperatureScale
															.getScaleTotal())
											/ (float) TemperatureScale
													.getScaleTotal();
								}
							} else {
								return 0.0f;
							}
						}

						EntityPlayer player = (EntityPlayer) entity;

						if (world == null)
							world = entity.worldObj;

						TemperatureHandler tempHandler = (TemperatureHandler) TemperatureHelper
								.getTemperatureData(player);
						int finalTemperature = tempHandler.debugger.targetTemperature;
						if (overrideThermometerLimits) {
							float clampedTemp = (float) MathHelper.clamp_double(
									finalTemperature, lowerBound, upperBound);
							float shiftedTemp = (clampedTemp - lowerBound);
							return shiftedTemp
									/ ((float) (upperBound - lowerBound));
						} else {
							return (float) MathHelper.clamp_double(
									finalTemperature, 0,
									TemperatureScale.getScaleTotal())
									/ (float) TemperatureScale.getScaleTotal();
						}
					}
				});
	}

	Map<UUID, Long> messageDebounce = new HashMap<UUID, Long>();

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn,
			World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			// Get the temperature of the world at the player's location.
			if (!messageDebounce.containsKey(player.getUniqueID())
					|| (System.currentTimeMillis() - messageDebounce
							.get(player.getUniqueID()) > 2000)) {
				BlockPos playerPosition = player.getPosition();
				int finalTemperature = TemperatureHandler
						.getTargetTemperatureAt(world, playerPosition);

				player.addChatMessage(new TextComponentTranslation(
						"item.thermometer.read", finalTemperature));
				messageDebounce.put(player.getUniqueID(),
						System.currentTimeMillis());
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}
}
