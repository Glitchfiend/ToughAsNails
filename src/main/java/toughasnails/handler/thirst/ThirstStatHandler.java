package toughasnails.handler.thirst;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.TANCapabilities;
import toughasnails.thirst.ThirstHandler;

public class ThirstStatHandler
{
    @SubscribeEvent
    public void onPlayerJump(LivingJumpEvent event)
    {
        World world = event.getEntity().world;

        if (!world.isRemote && event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            if (!player.isCreative()) {
                ThirstHandler thirstStats = (ThirstHandler) player.getCapability(TANCapabilities.THIRST, null);

                if (player.isSprinting()) {
                    thirstStats.addExhaustion(0.8F);
                } else {
                    thirstStats.addExhaustion(0.2F);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event)
    {
        World world = event.getEntity().world;

        if (!world.isRemote && event.getAmount() != 0.0F)
        {
            if (event.getEntity() instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer)event.getEntity();

                if (!player.isCreative())
                {
                    ThirstHandler thirstStats = (ThirstHandler) player.getCapability(TANCapabilities.THIRST, null);
                    //Uses hunger values for now, may change in the future
                    thirstStats.addExhaustion(event.getSource().getHungerDamage());
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerAttackEntity(AttackEntityEvent event)
    {
        World world = event.getEntity().world;
        Entity target = event.getTarget();
        
        if (!world.isRemote)
        {
            EntityPlayer player = event.getEntityPlayer();
            
            if (target.canBeAttackedWithItem() && !player.isCreative())
            {
                if (!target.hitByEntity(player))
                {
                    float attackDamage = (float)player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                    float weaponAttackDamage = 0.0F;
                    
                    if (target instanceof EntityLivingBase)
                    {
                        weaponAttackDamage = EnchantmentHelper.getModifierForCreature(player.getHeldItem(EnumHand.MAIN_HAND), ((EntityLivingBase)target).getCreatureAttribute());
                    }
                    else
                    {
                        weaponAttackDamage = EnchantmentHelper.getModifierForCreature(player.getHeldItem(EnumHand.MAIN_HAND), EnumCreatureAttribute.UNDEFINED);
                    }
                    
                    if (attackDamage > 0.0F || weaponAttackDamage > 0.0F)
                    {
                        boolean canAttack = target.attackEntityFrom(DamageSource.causePlayerDamage(player), 0.0F);
                        
                        if (canAttack)
                        {
                            //The only part of this method that is new - the rest is recreating the surrounding circumstances in attackTargetEntityWithCurrentItem
                            ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);
                            
                            thirstStats.addExhaustion(0.3F);
                        }
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event)
    {
        World world = event.getWorld();
        EntityPlayer player = event.getPlayer();
        BlockPos pos = event.getPos();
        IBlockState state = event.getState();
        
        if (!world.isRemote && !player.isCreative())
        {
            boolean canHarvestBlock = state.getBlock().canHarvestBlock(world, pos, player);
            
            if (canHarvestBlock)
            {
                //The only part of this method that is new - the rest is recreating the surrounding circumstances in func_180237_b
                ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);
                
                thirstStats.addExhaustion(0.025F);
            }
        }
    }
}
