package toughasnails.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import toughasnails.thirst.ThirstStats;

public class ItemBackpack extends ItemArmor
{
    public ItemBackpack(ItemArmor.ArmorMaterial material, int renderIndex)
    {
        // backpacks are always on your body - armorType = 1
        super(material, renderIndex, 1);        
    }
}
