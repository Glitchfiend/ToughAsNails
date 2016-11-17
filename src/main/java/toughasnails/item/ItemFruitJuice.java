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
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.item.ItemDrink;
import toughasnails.api.thirst.IDrink;
import toughasnails.item.ItemFruitJuice.JuiceType;

public class ItemFruitJuice extends ItemDrink<JuiceType>
{
    @Override
    public JuiceType getTypeFromMeta(int meta) 
    {
        return JuiceType.values()[meta % JuiceType.values().length];
    }
    
    // get the correct name for this item by looking up the meta value in the DartType enum
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return "item.juice_" + getTypeFromMeta(stack.getMetadata()).toString();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        for (JuiceType juiceType : JuiceType.values())
        {
            subItems.add(new ItemStack(item, 1, juiceType.ordinal()));
        }
    }
    
    public static enum JuiceType implements IDrink, IStringSerializable
    {
        APPLE(8, 0.8F), 
        BEETROOT(10, 0.8F), 
        CACTUS(9, 0.2F), 
        CARROT(8, 0.6F), 
        CHORUS_FRUIT(12, 0.6F),
        GLISTERING_MELON(16, 1.0F),
        GOLDEN_APPLE(20, 1.2F), 
        GOLDEN_CARROT(14, 1.0F), 
        MELON(8, 0.5F), 
        PUMPKIN(7, 0.7F);
        
        private int thirst;
        private float hydration;
        
        private JuiceType(int thirst, float hydration)
        {
            this.thirst = thirst;
            this.hydration = hydration;
        }
        
        @Override
        public int getThirst()
        {
            return this.thirst;
        }
        
        @Override
        public float getHydration()
        {
            return this.hydration;
        }
        
        @Override
        public float getPoisonChance() 
        {
            return 0.0F;
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
    
    @Override
    public boolean hasEffect(ItemStack stack)
    {
        switch (getTypeFromMeta(stack.getMetadata()))
        {
            case GLISTERING_MELON: case GOLDEN_APPLE: case GOLDEN_CARROT:
                return true;
            default:
                return super.hasEffect(stack); 
        }
    }
   
}
