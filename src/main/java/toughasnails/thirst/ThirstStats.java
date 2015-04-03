package toughasnails.thirst;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import toughasnails.api.PlayerStat;

public class ThirstStats extends PlayerStat
{
    private int thirstLevel;
    private int prevThirstLevel;
    private float thirstHydrationLevel;
    private float thirstExhaustionLevel;
    private int thirstTimer;
    
    private Vector3d movementVec;
    
    public ThirstStats(String identifier)
    {
        super(identifier);
    }

    @Override
    public void init(EntityPlayer player, World world)
    {
        this.thirstLevel = 20;
        this.thirstHydrationLevel = 5.0F;
    }
    
    @Override
    public void update(EntityPlayer player, World world, Phase phase)
    {  
        if (phase == Phase.START)
        {
            if (movementVec != null)
            {
                movementVec.sub(new Vector3d(player.posX, player.posY, player.posZ));
                int distance = (int)Math.round(movementVec.length() * 100.0F);
                
                if (distance > 0) applyMovementExhaustion(player, distance);
            }
        }
        else if (phase == Phase.END)
        {
            this.movementVec = new Vector3d(player.posX, player.posY, player.posZ);
            
            EnumDifficulty enumdifficulty = world.getDifficulty();

            if (this.thirstExhaustionLevel > 4.0F)
            {
                this.thirstExhaustionLevel -= 4.0F;

                if (this.thirstHydrationLevel > 0.0F)
                {
                    this.thirstHydrationLevel = Math.max(this.thirstHydrationLevel - 1.0F, 0.0F);
                }
                else if (enumdifficulty != EnumDifficulty.PEACEFUL)
                {
                    this.thirstLevel = Math.max(this.thirstLevel - 1, 0);
                }
            }

            if (this.thirstLevel <= 0)
            {
                ++this.thirstTimer;

                if (this.thirstTimer >= 80)
                {
                    if (player.getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD || player.getHealth() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL)
                    {
                        player.attackEntityFrom(DamageSource.starve, 1.0F);
                    }

                    this.thirstTimer = 0;
                }
            }
            else
            {
                this.thirstTimer = 0;
            }
            
            if (player.isSprinting() && thirstLevel <= 6)
            {
                player.setSprinting(false);
            }
        }
    }
    
    private void applyMovementExhaustion(EntityPlayer player, int distance)
    {
        if (player.isInsideOfMaterial(Material.water))
        {
            this.addExhaustion(0.015F * (float)distance * 0.01F);
        }
        else if (player.isInWater())
        {
            this.addExhaustion(0.015F * (float)distance * 0.01F);
        }
        else if (player.onGround)
        {
            if (player.isSprinting())
            {
                this.addExhaustion(0.099999994F * (float)distance * 0.01F);
            }
            else
            {
                this.addExhaustion(0.01F * (float)distance * 0.01F);
            }
        }
    }
    
    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
        compound.setInteger("thirstLevel", this.thirstLevel);
        compound.setInteger("thirstTimer", this.thirstTimer);
        compound.setFloat("thirstHydrationLevel", this.thirstHydrationLevel);
        compound.setFloat("thirstExhaustionLevel", this.thirstExhaustionLevel);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound)
    { 
        if (compound.hasKey("thirstLevel"))
        {
            this.thirstLevel = compound.getInteger("thirstLevel");
            this.thirstHydrationLevel = compound.getFloat("thirstHydrationLevel");
            this.thirstExhaustionLevel = compound.getFloat("thirstExhaustionLevel");
            this.thirstTimer = compound.getInteger("thirstTimer");
        }
    }

    @Override
    public boolean shouldUpdateClient()
    {
        return this.prevThirstLevel != this.thirstLevel;
    }
    
    @Override
    public void onSendClientUpdate()
    {
        this.prevThirstLevel = this.thirstLevel;
    }
    
    public void addExhaustion(float amount)
    {
        this.thirstExhaustionLevel = Math.min(this.thirstExhaustionLevel + amount, 40.0F);
    }
    
    public int getThirstLevel()
    {
        return this.thirstLevel;
    }
    
    public float getThirstHydrationLevel()
    {
        return this.thirstHydrationLevel;
    }
}
