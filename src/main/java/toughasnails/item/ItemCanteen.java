package toughasnails.item;

import java.util.List;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.TANCapabilities;
import toughasnails.api.TANPotions;
import toughasnails.api.thirst.WaterType;
import toughasnails.thirst.ThirstHandler;

public class ItemCanteen extends Item
{
    public ItemCanteen()
    {
        this.addPropertyOverride(new ResourceLocation("filled"), new IItemPropertyGetter()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World world, EntityLivingBase entity)
            {
                WaterType waterType = getWaterType(stack);
                
                if (waterType == null) return 0.0F;
                else return 1.0F;
            }
        });
        
        this.maxStackSize = 1;
        this.setMaxDamage(3);
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        WaterType waterType = getWaterType(stack);
        
        if (entity instanceof EntityPlayer && waterType != null)
        {
            EntityPlayer player = (EntityPlayer)entity;

            if (!player.capabilities.isCreativeMode)
            {
                int damage = (stack.getItemDamage() >> 2) + 1;
                int typeIndex = (waterType.ordinal() + 1);
                
                //Reset the canteen to its empty state
                if (damage == this.getMaxDamage())
                {
                    damage = 0;
                    typeIndex = 0;
                }
                
                this.setDamage(stack, typeIndex | (damage << 2));
            }

            if (!world.isRemote)
            {
                ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);
                thirstStats.addStats(waterType.getThirst(), waterType.getHydration());
                
                if (world.rand.nextFloat() < waterType.getPoisonChance())
                {
                    player.addPotionEffect(new PotionEffect(TANPotions.thirst, 100));
                }
            }
        }

        return stack;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        RayTraceResult movingObjectPos = this.getMovingObjectPositionFromPlayer(world, player, true);
        ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);
        WaterType waterType = getWaterType(stack);
        int damage = stack.getItemDamage() >> 2;
        
        if (waterType != null && getTimesUsed(stack) < 3 && thirstStats.isThirsty())
        {
            player.setActiveHand(hand);
        }
        else if (waterType == null || damage == 3)
        {
            if (movingObjectPos != null && movingObjectPos.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                BlockPos pos = movingObjectPos.getBlockPos();
                IBlockState state = world.getBlockState(pos);
                Fluid fluid = FluidRegistry.lookupFluidForBlock(state.getBlock());

                if (fluid != null && fluid == FluidRegistry.WATER) //Temporary, until a registry is created
                {
                    stack.setItemDamage(1);
                }
                else if (state.getBlock() instanceof BlockCauldron)
                {
                    BlockCauldron cauldron = (BlockCauldron)state.getBlock();
                    int level = ((Integer)state.getValue(BlockCauldron.LEVEL));
                    
                    if (level > 0 && !world.isRemote)
                    {
                        if (!player.capabilities.isCreativeMode)
                        {
                            player.addStat(StatList.cauldronUsed);
                            stack.setItemDamage(1);
                        }

                        cauldron.setWaterLevel(world, pos, state, level - 1);
                    }
                }
            }
        }
        
        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return (stack.getItemDamage() >> 2) / (double)stack.getMaxDamage();
    }

    private WaterType getWaterType(ItemStack stack)
    {
        int type = stack.getMetadata() & 3;
        return type > 0 ? WaterType.values()[type - 1] : null;
    }
    
    private int getTimesUsed(ItemStack stack)
    {
        return stack.getItemDamage() >> 2;
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
        WaterType type = getWaterType(itemStack);
        
        if (type != null)
        {
            stringList.add(type.getDescription());
        }
    }
}
