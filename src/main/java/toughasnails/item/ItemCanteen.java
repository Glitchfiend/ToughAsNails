package toughasnails.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
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

public class ItemCanteen extends ItemFluidContainer
{
    public ItemCanteen()
    {
        super(-1);
        
        this.maxStackSize = 1;
        this.setMaxDamage(3);
        this.capacity = 200; 
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player)
    {
        FluidStack canteenFluid = getFluid(stack);
        
        if (!player.capabilities.isCreativeMode)
        {
            this.drain(stack, 50, true);
            this.setDamage(stack, 4 - ((canteenFluid.amount - 50) / 50));
        }
        
        if (!world.isRemote)
        {
            ThirstStats thirstStats = (ThirstStats)player.getExtendedProperties("thirst");
        
            thirstStats.addStats(8, 0.8F);
        }

        return stack;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        MovingObjectPosition movingObjectPos = this.getMovingObjectPositionFromPlayer(world, player, true);
        FluidStack canteenFluid = getFluid(stack);
        ThirstStats thirstStats = (ThirstStats)player.getExtendedProperties("thirst");
        
        if (canteenFluid != null && canteenFluid.amount >= 50 && thirstStats.isThirsty())
        {
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));   
        }
        else if (canteenFluid == null || canteenFluid.amount != this.capacity)
        {
            if (movingObjectPos != null && movingObjectPos.typeOfHit == MovingObjectType.BLOCK)
            {
                BlockPos pos = movingObjectPos.getBlockPos();
                IBlockState state = world.getBlockState(pos);
                Fluid fluid = FluidRegistry.lookupFluidForBlock(state.getBlock());

                if (fluid != null && fluid == FluidRegistry.WATER) //Temporary, until a registry is created
                {
                    this.fill(stack, new FluidStack(fluid, this.capacity), true);
                    this.setDamage(stack, 0);
                }
            }
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
    
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List stringList, boolean showAdvancedInfo) 
    {
        FluidStack fluid = getFluid(itemStack);
        
        if (fluid != null && fluid.amount > 0)
        {
            String localizedName = fluid.getLocalizedName();

            stringList.add(localizedName);
        }
    }
}
