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
	protected int REFILLS = 500;
	public ItemCanteen() {
		this.addPropertyOverride(new ResourceLocation("filled"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, World world, EntityLivingBase entity) {
				WaterType waterType = getWaterType(stack);
				if (waterType == null) {
					return 0.0F;
				} else {
					return 1.0F;
				}
			}
		});
		this.maxStackSize = 1;
		this.setNoRepair();
	}
    
	private int getUses(NBTTagCompound comp) {
		if (comp == null) comp = new NBTTagCompound();
		int uses = comp.getInteger("uses");
		if (uses == 0) uses = -1;
		if ((uses > 0 && uses < 4) || uses == -1)
			return uses;
		return -1;
	}
    /**
     * 
     * @param stack
     * @param uses -1 = new, 0 = unassigned,  3 = 3 drinks left;
     */
	public NBTTagCompound setUses(NBTTagCompound comp, int uses){
		if (comp == null) comp = new NBTTagCompound();
		if ((uses > 0 && uses < 4) || uses == -1) {
			comp.setInteger("uses", uses);
		}
		return comp;
	}
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
		WaterType waterType = getWaterType(stack);
		if (entity instanceof EntityPlayer && waterType != null) {
			EntityPlayer player = (EntityPlayer)entity;
			if (!player.capabilities.isCreativeMode) {
				NBTTagCompound comp = stack.getTagCompound();
				int uses = getUses(comp) - 1;
				if (uses == 0) {
					uses = -1;
					comp.setInteger("water_type", 0);
				}
				stack.setTagCompound(doLore(setUses(comp, uses), stack)); 
			}
			ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);
			thirstStats.addStats(waterType.getThirst(), waterType.getHydration());
			if (!world.isRemote && world.rand.nextFloat() < waterType.getPoisonChance() && SyncedConfigHandler.getBooleanValue(GameplayOption.ENABLE_THIRST)
			) {
				player.addPotionEffect(new PotionEffect(TANPotions.thirst, 600));
			}
		}
		return stack;
	}
    
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);
		WaterType waterType = getWaterType(stack);
		if (getUses(stack.getTagCompound()) > 0 && thirstStats.isThirsty()){
			if (player.isSneaking()) {
				if (!attemptCanteenFill(player, stack)) player.setActiveHand(hand);
			} else {
				player.setActiveHand(hand);
			}
		} else {
			attemptCanteenFill(player, stack);
		}
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}
	@Override
	public boolean showDurabilityBar(ItemStack stack){
		return getUses(stack.getTagCompound()) > 0 ? true : false;
	}
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return (3d - (double)getUses(stack.getTagCompound())) / 3d;
	}
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return MathHelper.hsvToRGB(Math.max(0.0F, (float)getUses(stack.getTagCompound()) / 3) / 3.0F, 1.0F, 1.0F);
	}
	private int getRefills(NBTTagCompound comp){
		if (comp == null) comp = new NBTTagCompound();
		int refills = comp.getInteger("refills");
		return refills;
	}
    /**
     * Handles lore for item display
     * @param comp compound to modify 
     * @param stack item stack in question
     * @return compound with lore
     */
    private NBTTagCompound doLore(NBTTagCompound comp, ItemStack stack){
   	 	return setLore(comp, new String[] {"Refills: " + getRefills(comp) + " / " + REFILLS, "Uses Left: " + (getUses(comp) > 0 ? getUses(comp) : "0") + " / 3"}); //TODO % value at the end
    }
    /**
     * handles refill action
     * @param comp Tag compound to work off of
     * @param stack item stack in question
     * @param player player involved
     * @return modified tag compound
     */
	private NBTTagCompound doRefill(NBTTagCompound comp, ItemStack stack, EntityPlayer player) {
		int ref = comp.getInteger("refills");
		int ren = ref - 1;
		if (ref == 0) { //no tag found
			comp.setInteger("refills", REFILLS); 
			comp = doLore(comp, stack);
		} else {
			if (ren == 1) {
				if (!player.capabilities.isCreativeMode) {
					comp.setInteger("refills", ren); 
					comp = doLore(comp, stack);
					player.renderBrokenItemStack(stack);
					stack.shrink(1);
					player.addStat(StatList.getObjectBreakStats(stack.getItem()));
				}
			} else {
				comp.setInteger("refills", ren);
				comp = doLore(comp, stack);
			}
		}
		return comp;
	}
    /**
     * Attempt to fill the provided canteen stack with water.
     * @param player The player holding the canteen.
     * @param stack The canteen item stack.
     * @return true if successful, otherwise false.
     */
	public boolean attemptCanteenFill(EntityPlayer player, ItemStack stack) {
		World world = player.world;
		RayTraceResult movingObjectPos = this.rayTrace(world, player, true);
		NBTTagCompound comp = stack.getTagCompound();
		if (comp == null) comp = new NBTTagCompound();
		if (getUses(comp) < 3) {
			if (movingObjectPos != null && movingObjectPos.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos pos = movingObjectPos.getBlockPos();
				IBlockState state = world.getBlockState(pos);
				Fluid fluid = FluidRegistry.lookupFluidForBlock(state.getBlock());
				if (fluid != null && fluid == FluidRegistry.WATER) { //Temporary, until a registry is created
					comp.setInteger("water_type", 1);
					NBTTagCompound tag = doRefill(setUses(comp, 3), stack, player);
					if (!stack.isEmpty) stack.setTagCompound(tag); 
					return true;
				} else if (state.getBlock() instanceof BlockCauldron) {
					BlockCauldron cauldron = (BlockCauldron)state.getBlock();
					int level = ((Integer)state.getValue(BlockCauldron.LEVEL));
					if (level > 0 && !world.isRemote) {
						player.addStat(StatList.CAULDRON_USED);
						comp.setInteger("water_type", 1);
						stack.setTagCompound(doRefill(setUses(comp, 3), stack, player)); 
						if (!player.capabilities.isCreativeMode) {
							cauldron.setWaterLevel(world, pos, state, level - 1);
							return true;
						}
						return true;
					}
				} else if (state.getBlock() instanceof  BlockRainCollector) {
					int level = ((Integer)state.getValue(BlockRainCollector.LEVEL)).intValue();
					if (level > 0){
						BlockRainCollector collector = (BlockRainCollector)state.getBlock();
						comp.setInteger("water_type", 3);
						stack.setTagCompound(doRefill(setUses(comp, 3), stack, player)); 
						if (player.capabilities.isCreativeMode) {
							return true;
						}
						collector.setWaterLevel(world, pos, state, level - 1);
						return true;
					}
				}
			}
		}
		return false;
	}

	private WaterType getWaterType(ItemStack stack) {
		NBTTagCompound comp = stack.getTagCompound();
		if (comp == null) comp = new NBTTagCompound();
		int type = comp.getInteger("water_type");
		return type > 0 ? WaterType.values()[type - 1] : null;
	}
    
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}
	/**
	 * sets the items lore
	 * @param stack item stack to set lore on
	 * @param lore string list of lore to add (each string is a line)
	 */
	public NBTTagCompound setLore(NBTTagCompound comp, String[] lore){
		NBTTagList list = new NBTTagList();
		for (String s : lore){
			if (s != null && s != ""){
				list.appendTag(new NBTTagString(s));
			}
		}
		if (comp == null) comp = new NBTTagCompound();
		NBTTagCompound  dt = comp.getCompoundTag("display");
		if (dt == null) dt = new NBTTagCompound();
		dt.setTag("Lore", list);
		comp.setTag("display", dt);
		return comp;
	}
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		WaterType type = getWaterType(stack);
		if (type != null) {
			return "item." + type.toString().toLowerCase() + "_water_canteen";
		} else {
			return "item.empty_canteen";
		}
	}
}
