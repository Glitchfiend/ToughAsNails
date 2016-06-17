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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.season.SeasonHelper;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.season.SeasonTime;
import toughasnails.temperature.TemperatureHandler;

public class ItemThermometer extends Item
{
    public ItemThermometer()
    {
        this.addPropertyOverride(new ResourceLocation("temperature"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            double field_185088_a;
            @SideOnly(Side.CLIENT)
            double field_185089_b;
            @SideOnly(Side.CLIENT)
            int ticks;
            
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World world, EntityLivingBase entity)
            {
                if (entity == null || !(entity instanceof EntityPlayer))
                    return 0.0F;
                
                EntityPlayer player = (EntityPlayer)entity;
                
                if (world == null) world = entity.worldObj;

                TemperatureHandler tempHandler = (TemperatureHandler)TemperatureHelper.getTemperatureData(player);
                return (float)MathHelper.clamp_double(tempHandler.debugger.targetTemperature, 0, TemperatureScale.getScaleTotal()) / (float)TemperatureScale.getScaleTotal();
            }
        });
    }
}
