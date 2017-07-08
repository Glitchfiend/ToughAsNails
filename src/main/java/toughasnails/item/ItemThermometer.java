/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureHandler;
import toughasnails.temperature.modifier.AltitudeModifier;
import toughasnails.temperature.modifier.BiomeModifier;
import toughasnails.temperature.modifier.ObjectProximityModifier;
import toughasnails.temperature.modifier.SeasonModifier;
import toughasnails.temperature.modifier.TimeModifier;
import toughasnails.temperature.modifier.WeatherModifier;

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
						if (entity == null
								|| !(entity instanceof EntityPlayer)) {
							Entity frame = stack.getItemFrame();
							if (frame != null) {
								BlockPos framePosition = frame.getPosition();
								World frameWorld = frame.getEntityWorld();
								final TemperatureDebugger debugger = new TemperatureDebugger();
								AltitudeModifier altitudeModifier = new AltitudeModifier(
										debugger);
								BiomeModifier biomeModifier = new BiomeModifier(
										debugger);
								ObjectProximityModifier objectProximityModifier = new ObjectProximityModifier(
										debugger);
								WeatherModifier weatherModifier = new WeatherModifier(
										debugger);
								TimeModifier timeModifier = new TimeModifier(
										debugger);
								SeasonModifier seasonModifier = new SeasonModifier(
										debugger);

								Temperature baseTemperature = new Temperature(
										TemperatureHandler.TEMPERATURE_SCALE_MIDPOINT);
								Temperature targetTemperature = biomeModifier
										.modifyTarget(frameWorld, framePosition,
												baseTemperature);
								targetTemperature = altitudeModifier
										.modifyTarget(frameWorld, framePosition,
												targetTemperature);
								targetTemperature = objectProximityModifier
										.modifyTarget(frameWorld, framePosition,
												targetTemperature);
								targetTemperature = weatherModifier
										.modifyTarget(frameWorld, framePosition,
												targetTemperature);
								targetTemperature = timeModifier.modifyTarget(
										frameWorld, framePosition,
										targetTemperature);
								targetTemperature = seasonModifier.modifyTarget(
										frameWorld, framePosition,
										targetTemperature);

								int finalTemperature = targetTemperature
										.getRawValue();
								return (float) MathHelper
										.clamp_double(finalTemperature, 0,
												TemperatureScale
														.getScaleTotal())
										/ (float) TemperatureScale
												.getScaleTotal();
							} else {
								return 0.0f;
							}
						}

						EntityPlayer player = (EntityPlayer) entity;

						if (world == null)
							world = entity.worldObj;

						TemperatureHandler tempHandler = (TemperatureHandler) TemperatureHelper
								.getTemperatureData(player);
						return (float) MathHelper.clamp_double(
								tempHandler.debugger.targetTemperature, 0,
								TemperatureScale.getScaleTotal())
								/ (float) TemperatureScale.getScaleTotal();
					}
				});
	}
}
