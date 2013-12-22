package tan.eventhandler;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import tan.api.thirst.TANDrinkContainer;
import tan.api.thirst.TANDrinkInfo;
import tan.api.utils.TANPlayerStatUtils;
import tan.core.TANPotions;
import tan.network.PacketTypeHandler;
import tan.network.packet.PacketFinishedDrinking;
import tan.stats.ThirstStat;

public class ThirstItemEventHandler
{
    @ForgeSubscribe
    public void onEntityLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            World world = player.worldObj;

            if (world.isRemote)
            {
                if (player.isUsingItem())
                {
                    ItemStack itemInUse = player.getItemInUse();

                    if (itemInUse != null && FluidContainerRegistry.isContainer(itemInUse) && TANDrinkContainer.contains(itemInUse.itemID, 0))
                    {
                        FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemInUse);

                        if (fluidStack != null)
                        {
                            if (player.getItemInUseDuration() == itemInUse.getMaxItemUseDuration())
                            {
                                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketFinishedDrinking(itemInUse.itemID, itemInUse.getItemDamage(), fluidStack.getFluid().getName(), fluidStack.amount)));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void onFinishedDrinking(EntityPlayer player, int itemID, int metadata, String fluidName, int fluidAmount)
    {
        if (player != null)
        {
            World world = player.worldObj;

            FluidStack fluidStack = new FluidStack(FluidRegistry.getFluid(fluidName), fluidAmount);
            ThirstStat thirstStat = TANPlayerStatUtils.getPlayerStat(player, ThirstStat.class);

            if (fluidStack.amount != 0)
            {
                TANDrinkInfo drinkInfo = TANDrinkInfo.getDrinkInfo(fluidName);

                thirstStat.addThirst(drinkInfo.thirstAmount / (1000 / fluidStack.amount), drinkInfo.hydrationModifier / (1000 / fluidStack.amount));

                if (world.rand.nextFloat() < drinkInfo.poisoningChance)
                {
                    player.addPotionEffect(new PotionEffect(TANPotions.waterPoisoning.id, 1200, 0));
                }
            }

            TANPlayerStatUtils.setPlayerStat(player, thirstStat);
        }
    }

    @ForgeSubscribe
    public void onFluidRegister(FluidContainerRegistry.FluidContainerRegisterEvent event)
    {
        FluidContainerRegistry.FluidContainerData data = event.data;
        ItemStack filledContainer = data.filledContainer;

        if (filledContainer.itemID == Item.potion.itemID && filledContainer.getItemDamage() == 0)
        {
            data.fluid.amount = 330;
        }
    }

    @ForgeSubscribe
    public void onItemUse(PlayerInteractEvent event)
    {
        EntityPlayer player = event.entityPlayer;
        World world = player.worldObj;

        MovingObjectPosition pos = getMovingObjectPositionFromPlayer(world, player, true);
        ItemStack equippedItem = player.getCurrentEquippedItem();

        if (equippedItem != null && equippedItem.itemID == Item.glassBottle.itemID)
        {
            if (pos != null)
            {
                int x = pos.blockX;
                int y = pos.blockY;
                int z = pos.blockZ;

                if (!world.isRemote && world.getBlockId(x, y, z) == Block.waterStill.blockID)
                {
                    world.setBlockToAir(x, y, z);
                }
            }
        }
    }

    protected MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer, boolean par3)
    {
        float f = 1.0F;
        float f1 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * f;
        float f2 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * f;
        double d0 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * (double)f;
        double d1 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * (double)f + (double)(par1World.isRemote ? par2EntityPlayer.getEyeHeight() - par2EntityPlayer.getDefaultEyeHeight() : par2EntityPlayer.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
        double d2 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * (double)f;
        Vec3 vec3 = par1World.getWorldVec3Pool().getVecFromPool(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        if (par2EntityPlayer instanceof EntityPlayerMP)
        {
            d3 = ((EntityPlayerMP)par2EntityPlayer).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
        return par1World.rayTraceBlocks_do_do(vec3, vec31, par3, !par3);
    }
}
