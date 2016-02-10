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

public class ItemRespirator extends ItemArmor
{
    public ItemRespirator(ItemArmor.ArmorMaterial material, int renderIndex)
    {
        // respirators are always on your head - armorType = 0
        super(material, renderIndex, 0);        
    }
}
