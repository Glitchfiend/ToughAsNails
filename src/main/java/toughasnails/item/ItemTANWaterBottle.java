/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.item.ItemDrink;
import toughasnails.api.thirst.IDrink;
import toughasnails.api.thirst.WaterType;
import toughasnails.item.ItemTANWaterBottle.WaterBottleType;

public class ItemTANWaterBottle extends ItemDrink<WaterBottleType>
{
    @Override
    public WaterBottleType getTypeFromMeta(int meta) 
    {
        return WaterBottleType.values()[meta % WaterBottleType.values().length];
    }
    
    // get the correct name for this item by looking up the meta value in the DartType enum
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return "item." + getTypeFromMeta(stack.getMetadata()).toString() + "_water_bottle";
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems)
    {
        for (WaterBottleType waterBottleType : WaterBottleType.values())
        {
            subItems.add(new ItemStack(item, 1, waterBottleType.ordinal()));
        }
    }
    
    public static enum WaterBottleType implements IDrink, IStringSerializable
    {
        DIRTY(WaterType.DIRTY), 
        FILTERED(WaterType.FILTERED);
        
        private WaterType type;
        
        private WaterBottleType(WaterType type)
        {
            this.type = type;
        }
        
        @Override
        public int getThirst()
        {
            return type.getThirst();
        }
        
        @Override
        public float getHydration()
        {
            return type.getHydration();
        }
        
        @Override
        public float getPoisonChance()
        {
            return type.getPoisonChance();
        }
        
        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
        
        @Override
        public String toString()
        {
            return this.getName();
        }
    }
}
