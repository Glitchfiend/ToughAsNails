package toughasnails.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.item.TANItems;
import toughasnails.core.ToughAsNails;
import toughasnails.entities.projectile.EntityIceball;
import toughasnails.particle.TANParticleTypes;

public class EntityFreeze extends EntityMob implements IMob
{
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;

    public EntityFreeze(World worldIn)
    {
        super(worldIn);
        this.experienceValue = 10;
        this.tasks.addTask(4, new EntityFreeze.AIFireballAttack(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
    }

    /*protected String getLivingSound()
    {
        return "mob.freeze.breathe";
    }

    protected String getHurtSound()
    {
        return "mob.freeze.hit";
    }

    protected String getDeathSound()
    {
        return "mob.freeze.death";
    }*/

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float partialTicks)
    {
        return 15728880;
    }
    
    @Override
    public float getBrightness(float partialTicks)
    {
        return 1.0F;
    }

    @Override
    public void onLivingUpdate()
    {
        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }

        if (this.world.isRemote)
        {
            /*if (this.rand.nextInt(24) == 0 && !this.isSilent())
            {
                this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
            }*/

            for (int i = 0; i < 2; ++i)
            {
            	ToughAsNails.proxy.spawnParticle(TANParticleTypes.SNOWFLAKE, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }

        super.onLivingUpdate();
    }

    @Override
    protected void updateAITasks()
    {
        if (this.isBurning())
        {
            this.attackEntityFrom(DamageSource.inFire, 1.0F);
        }

        --this.heightOffsetUpdateTime;

        if (this.heightOffsetUpdateTime <= 0)
        {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
        }

        EntityLivingBase entitylivingbase = this.getAttackTarget();

        if (entitylivingbase != null && entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset)
        {
            this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
            this.isAirBorne = true;
        }

        super.updateAITasks();
    }

    @Override
    public void fall(float distance, float damageMultiplier)
    {
    }

    @Override
    protected Item getDropItem()
    {
        return TANItems.freeze_rod;
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        if (p_70628_1_)
        {
            int i = this.rand.nextInt(2 + p_70628_2_);

            for (int j = 0; j < i; ++j)
            {
                this.dropItem(TANItems.freeze_rod, 1);
            }
        }
    }

    @Override
    protected boolean isValidLightLevel()
    {
        return true;
    }

    static class AIFireballAttack extends EntityAIBase
        {
            private EntityFreeze freeze;
            private int field_179467_b;
            private int field_179468_c;

            public AIFireballAttack(EntityFreeze p_i45846_1_)
            {
                this.freeze = p_i45846_1_;
                this.setMutexBits(3);
            }

            public boolean shouldExecute()
            {
                EntityLivingBase entitylivingbase = this.freeze.getAttackTarget();
                return entitylivingbase != null && entitylivingbase.isEntityAlive();
            }

            public void startExecuting()
            {
                this.field_179467_b = 0;
            }

            public void updateTask()
            {
                --this.field_179468_c;
                EntityLivingBase entitylivingbase = this.freeze.getAttackTarget();
                double d0 = this.freeze.getDistanceSqToEntity(entitylivingbase);

                if (d0 < 4.0D)
                {
                    if (this.field_179468_c <= 0)
                    {
                        this.field_179468_c = 20;
                        this.freeze.attackEntityAsMob(entitylivingbase);
                    }

                    this.freeze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
                }
                else if (d0 < 256.0D)
                {
                    double d1 = entitylivingbase.posX - this.freeze.posX;
                    double d2 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (this.freeze.posY + (double)(this.freeze.height / 2.0F));
                    double d3 = entitylivingbase.posZ - this.freeze.posZ;

                    if (this.field_179468_c <= 0)
                    {
                        ++this.field_179467_b;

                        if (this.field_179467_b == 1)
                        {
                            this.field_179468_c = 60;
                        }
                        else if (this.field_179467_b <= 4)
                        {
                            this.field_179468_c = 6;
                        }
                        else
                        {
                            this.field_179468_c = 100;
                            this.field_179467_b = 0;
                        }

                        if (this.field_179467_b > 1)
                        {
                            float f = MathHelper.sqrt_float(MathHelper.sqrt_double(d0)) * 0.5F;
                            this.freeze.world.playEvent((EntityPlayer)null, 1009, new BlockPos((int)this.freeze.posX, (int)this.freeze.posY, (int)this.freeze.posZ), 0);

                            for (int i = 0; i < 1; ++i)
                            {
                                EntityIceball entityiceball = new EntityIceball(this.freeze.world, this.freeze, d1 + this.freeze.getRNG().nextGaussian() * (double)f, d2, d3 + this.freeze.getRNG().nextGaussian() * (double)f);
                                entityiceball.posY = this.freeze.posY + (double)(this.freeze.height / 2.0F) + 0.5D;
                                this.freeze.world.spawnEntityInWorld(entityiceball);
                            }
                        }
                    }

                    this.freeze.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
                }
                else
                {
                    this.freeze.getNavigator().clearPathEntity();
                    this.freeze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
                }

                super.updateTask();
            }
        }
}