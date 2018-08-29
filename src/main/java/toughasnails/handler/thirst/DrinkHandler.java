/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.thirst;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import toughasnails.api.TANPotions;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.item.ItemDrink;
import toughasnails.api.stat.capability.IThirst;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.api.thirst.WaterType;
import toughasnails.config.json.DrinkData;
import toughasnails.fluids.blocks.BlockPurifiedWaterFluid;
import toughasnails.handler.PacketHandler;
import toughasnails.init.ModConfig;
import toughasnails.network.message.MessageDrinkWaterInWorld;
import toughasnails.thirst.ThirstHandler;

public class DrinkHandler
{
    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            ItemStack stack = event.getItem();
            ThirstHandler thirstHandler = (ThirstHandler)ThirstHelper.getThirstData(player);

            if (thirstHandler.isThirsty())
            {
                // For some reason the stack size can be zero for water bottles, which breaks everything.
                // As a workaround, we temporarily set it to 1
                boolean zeroStack = false;

                if (stack.getCount() <= 0)
                {
                    stack.setCount(1);
                    zeroStack = true;
                }

                // Special case potions because they use NBT
                if (stack.getItem().equals(Items.POTIONITEM))
                {
                    if (PotionUtils.getFullEffectsFromItem(stack).isEmpty())
                    {
                        thirstHandler.addStats(WaterType.NORMAL.getThirst(), WaterType.NORMAL.getHydration());
                        if (player.world.rand.nextFloat() < WaterType.NORMAL.getPoisonChance() && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST))
                        {
                            player.addPotionEffect(new PotionEffect(TANPotions.thirst, 600));
                        }
                    }
                    else
                    {
                        //Still fill thirst for other potions, but less than water
                        thirstHandler.addStats(4, 0.3F);
                    }
                }
                else if (!(stack.getItem() instanceof ItemDrink))
                {
                    String registryName = stack.getItem().getRegistryName().toString();

                    if (ModConfig.drinkData.containsKey(registryName))
                    {
                        for (DrinkData drinkData : ModConfig.drinkData.get(registryName))
                        {
                            if (drinkData.getPredicate().apply(stack))
                            {
                                applyDrinkFromData(player, drinkData);
                                break;
                            }
                        }
                    }
                }

                if (zeroStack) stack.setCount(0);
            }
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event)
    {
        // only do tryDrinkWaterInWorld on client-side if the player is sneaking and has an empty main hand
        if (canWorldDrink(event) && event.getEntityPlayer().isSneaking())
            tryDrinkWaterInWorld(event.getEntityPlayer(), true);
    }

    // RightClickEmpty is only fired client-side, so we use a custom packet for either case
    @SubscribeEvent
    public void RightClickEmpty(final PlayerInteractEvent.RightClickEmpty event)
    {
        // only do tryDrinkWaterInWorld on client-side if the player is sneaking and has an empty main hand
        if (canWorldDrink(event) && event.getEntityPlayer().isSneaking())
            tryDrinkWaterInWorld(event.getEntityPlayer(), true);
    }

    private boolean canWorldDrink(final PlayerInteractEvent event) {
        return (SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST_WORLD) || SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST_RAIN)) &&
                (EnumHand.MAIN_HAND == event.getHand()) &&
                (event.getEntityPlayer().getHeldItemMainhand().isEmpty()) &&
                (ThirstHelper.getThirstData(event.getEntityPlayer()).getThirst() < 20) &&
                (Side.CLIENT == event.getSide());
    }

    public static void tryDrinkWaterInWorld(final EntityPlayer player, final boolean isClient) {
        final TargetWater targetWater = getRightClickedWater(player);
        if (null != targetWater) {
            if (isClient) {
                // send server packet
                PacketHandler.instance.sendToServer(new MessageDrinkWaterInWorld());
                // play drink sound
                player.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5f, 1.0f);
                // swing hand
                player.swingArm(EnumHand.MAIN_HAND);
            } else {
                // do thirst
                applyDrinkFromWaterType(player, targetWater.type);
                if (targetWater.type == WaterType.PURIFIED) {
                    player.world.setBlockToAir(targetWater.pos);
                }
            }
        }
    }

    // used by both client (pre-packet) and server
    private static TargetWater getRightClickedWater(final EntityPlayer player) {
        // first do rain check (cheaper)
        if (SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST_RAIN) &&
                player.world.isRainingAt(player.getPosition()) &&
                (-75.0f > player.rotationPitch) && // 75 degrees is mostly upwards
                (player.world.canSeeSky(player.getPosition())))
        {
            return new TargetWater(null, WaterType.RAIN);
        } else if (SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST_WORLD)) {
            // do raytrace for liquid blocks within reach distance of player
            final Vec3d vecPlayerOrigin = player.getPositionEyes(1.0f);
            final Vec3d vecPlayerLook = player.getLook(1.0f);
            final double playerReachDistance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() * 0.5D;
            final Vec3d vecPlayerSee = vecPlayerOrigin.addVector(vecPlayerLook.x * playerReachDistance, vecPlayerLook.y * playerReachDistance, vecPlayerLook.z * playerReachDistance);
            final RayTraceResult raytraceResult = player.getEntityWorld().rayTraceBlocks(vecPlayerOrigin, vecPlayerSee, true, false, false);
            if ((null != raytraceResult) && (RayTraceResult.Type.BLOCK == raytraceResult.typeOfHit)) {
                final Block block = player.getEntityWorld().getBlockState(raytraceResult.getBlockPos()).getBlock();
                if (block instanceof BlockPurifiedWaterFluid) {
                    return new TargetWater(raytraceResult.getBlockPos(), WaterType.PURIFIED);
                } else if (Blocks.WATER == block) {
                    return new TargetWater(raytraceResult.getBlockPos(), WaterType.NORMAL);
                }
            }
        }
        return null;
    }

    private static void applyDrinkFromData(final EntityPlayer player, final DrinkData data)
    {
        applyDrink(player, data.getThirstRestored(), data.getHydrationRestored(), data.getPoisonChance());
    }

    private static void applyDrinkFromWaterType(final EntityPlayer player, final WaterType waterType)
    {
        applyDrink(player, waterType.getThirst(), waterType.getHydration(), waterType.getPoisonChance());
    }

    private static void applyDrink(final EntityPlayer player, final int thirstRestored, final float hydrationRestored, final float poisonChance)
    {
        IThirst thirstStats = ThirstHelper.getThirstData(player);
        thirstStats.addStats(thirstRestored, hydrationRestored);

        if (!player.world.isRemote && (player.world.rand.nextFloat() < poisonChance) && SyncedConfig.getBooleanValue(GameplayOption.ENABLE_THIRST))
        {
            player.addPotionEffect(new PotionEffect(TANPotions.thirst, 600));
        }
    }

    private static class TargetWater
    {
        final BlockPos pos;
        final WaterType type;
        
        TargetWater(@Nullable BlockPos pos, WaterType type)
        {
            this.pos = pos;
            this.type = type;
        }
    }
}
