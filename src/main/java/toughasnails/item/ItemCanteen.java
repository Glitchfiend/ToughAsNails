package toughasnails.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import toughasnails.api.TANCapabilities;
import toughasnails.thirst.ThirstHandler;

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
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        FluidStack canteenFluid = getFluid(stack);
        
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;

            if (!player.capabilities.isCreativeMode)
            {
                this.drain(stack, 50, true);
                this.setDamage(stack, 4 - ((canteenFluid.amount - 50) / 50));
            }

            if (!world.isRemote)
            {
                ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);

                thirstStats.addStats(8, 0.8F);
            }
        }

        return stack;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        RayTraceResult movingObjectPos = this.getMovingObjectPositionFromPlayer(world, player, true);
        FluidStack canteenFluid = getFluid(stack);
        ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);
        
        if (canteenFluid != null && canteenFluid.amount >= 50 && thirstStats.isThirsty())
        {
            player.setActiveHand(hand);
        }
        else if (canteenFluid == null || canteenFluid.amount != this.capacity)
        {
            if (movingObjectPos != null && movingObjectPos.typeOfHit == RayTraceResult.Type.BLOCK)
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
        
        return new ActionResult(EnumActionResult.SUCCESS, stack);
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
