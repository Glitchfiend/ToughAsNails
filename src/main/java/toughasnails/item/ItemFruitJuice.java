/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.capability.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.api.thirst.WaterType;
import toughasnails.thirst.ThirstHandler;

public class ItemFruitJuice extends Item
{
    public ItemFruitJuice()
    {
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        ThirstHandler thirstHandler = (ThirstHandler)ThirstHelper.getThirstData(player);
        
        if (thirstHandler.isThirsty())
        {
            player.setActiveHand(hand);
        }

        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;
            IThirst thirst = ThirstHelper.getThirstData(player);
            JuiceType type = JuiceType.fromMeta(stack.getMetadata());
            
            thirst.addStats(type.getThirst(), type.getHydration());
            return new ItemStack(Items.glass_bottle);
        }

        return stack;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }
    
    // default behavior in Item is to return 0, but the meta value is important here because it determines which dart type to use
    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }

    // get the correct name for this item by looking up the meta value in the DartType enum
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return "item.juice_" + JuiceType.fromMeta(stack.getMetadata()).toString();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List subItems)
    {
        for (JuiceType juiceType : JuiceType.values())
        {
            subItems.add(new ItemStack(item, 1, juiceType.ordinal()));
        }
    }
    
    public static enum JuiceType implements IStringSerializable
    {
        APPLE(5, 0.6F), 
        BEETROOT(9, 0.6F), 
        CACTUS(7, 0.1F), 
        CARROT(4, 0.5F), 
        GOLDEN_APPLE(15, 1.0F), 
        GOLDEN_CARROT(13, 0.8F), 
        MELON(5, 0.3F), 
        PUMPKIN(3, 0.2F);
        
        private int thirst;
        private float hydration;
        
        private JuiceType(int thirst, float hydration)
        {
            this.thirst = thirst;
            this.hydration = hydration;
        }
        
        public int getThirst()
        {
            return this.thirst;
        }
        
        public float getHydration()
        {
            return this.hydration;
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
        
        public static JuiceType fromMeta(int meta)
        {
            return JuiceType.values()[meta % JuiceType.values().length];
        }
    }
}
