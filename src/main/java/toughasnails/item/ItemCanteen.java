package toughasnails.item;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
import toughasnails.config.GameplayOption;
import toughasnails.config.SyncedConfigHandler;
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
        this.setNoRepair();
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

            ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);
            thirstStats.addStats(waterType.getThirst(), waterType.getHydration());
            
            if (!world.isRemote && world.rand.nextFloat() < waterType.getPoisonChance() && SyncedConfigHandler.getBooleanValue(GameplayOption.ENABLE_THIRST))
            {
                player.addPotionEffect(new PotionEffect(TANPotions.thirst, 600));
            }
        }

        return stack;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);
        WaterType waterType = getWaterType(stack);
        
        if (!attemptCanteenFill(player, stack) && waterType != null && getTimesUsed(stack) < 3 && thirstStats.isThirsty())
        {
            player.setActiveHand(hand);
        }
        
        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return (stack.getItemDamage() >> 2) / (double)stack.getMaxDamage();
    }
    
    /**
     * Attempt to fill the provided canteen stack with water.
     * @param player The player holding the canteen.
     * @param stack The canteen item stack.
     * @return true if successful, otherwise false.
     */
    private boolean attemptCanteenFill(EntityPlayer player, ItemStack stack)
    {
        World world = player.world;
        RayTraceResult movingObjectPos = this.rayTrace(world, player, true);
        
        if (movingObjectPos != null && movingObjectPos.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = movingObjectPos.getBlockPos();
            IBlockState state = world.getBlockState(pos);
            Fluid fluid = FluidRegistry.lookupFluidForBlock(state.getBlock());

            if (fluid != null && fluid == FluidRegistry.WATER) //Temporary, until a registry is created
            {
                stack.setItemDamage(1);
                return true;
            }
            else if (state.getBlock() instanceof BlockCauldron)
            {
                BlockCauldron cauldron = (BlockCauldron)state.getBlock();
                int level = ((Integer)state.getValue(BlockCauldron.LEVEL));
                
                if (level > 0 && !world.isRemote)
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        player.addStat(StatList.CAULDRON_USED);
                        stack.setItemDamage(1);
                        return true;
                    }

                    cauldron.setWaterLevel(world, pos, state, level - 1);
                }
            }
        }
        
        return false;
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
    public String getUnlocalizedName(ItemStack stack)
    {
        WaterType type = getWaterType(stack);
        
        if (type != null)
        {
        	return "item." + type.toString().toLowerCase() + "_water_canteen";
        }
        else
        {
        	return "item.empty_canteen";
        }
    }
}
