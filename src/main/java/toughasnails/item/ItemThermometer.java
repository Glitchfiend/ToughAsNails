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
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureScale;

public class ItemThermometer extends Item
{
    public ItemThermometer()
    {
        this.addPropertyOverride(new ResourceLocation("temperature"), new IItemPropertyGetter()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
            {
                if (entity == null && !stack.isOnItemFrame())
                    return TemperatureScale.getScaleMidpoint();
                    
                Entity entity = entityIn;
                if (stack.isOnItemFrame())
                    entity = stack.getItemFrame();

                World world = worldIn;
                if (world == null)
                    world = entity.world;

                return (float) TemperatureHelper.getTargetAtPos(world, entity.getPosition(), null).getRawValue() / (float) TemperatureScale.getScaleTotal();
            }
        });
    }
}
