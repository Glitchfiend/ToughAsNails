/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureHandler;

public class ItemThermometer extends Item
{
    public ItemThermometer()
    {
        this.addPropertyOverride(new ResourceLocation("temperature"), new IItemPropertyGetter()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World world, EntityLivingBase entity)
            {
                float targetTemperature = 0.0F;

                if (entity instanceof EntityPlayer || (entity == null && (entity = Minecraft.getMinecraft().player) != null) )
                {
                    EntityPlayer player = (EntityPlayer) entity;
                    TemperatureHandler tempHandler = (TemperatureHandler) TemperatureHelper.getTemperatureData(player);
                    targetTemperature = tempHandler.debugger.targetTemperature;
                }

                return MathHelper.clamp(targetTemperature, 0, TemperatureScale.getScaleTotal()) / (float)TemperatureScale.getScaleTotal();
            }
        });
    }
}
