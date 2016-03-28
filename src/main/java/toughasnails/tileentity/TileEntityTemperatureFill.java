/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.tileentity;

import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityTemperatureFill extends TileEntity implements ITickable
{
    //TODO: Queue is unnecessary, set prevents duplicates anyway
    //TODO: Track obstructions seperately, prevents unnecessarily checking adjacents and only checks obstructions that matter
    //TODO: Slower verification intervals the further away from the base position
    
    private Set<BlockPos>[] currentTrackedPositions;
    private Set<BlockPos> queuedTrackedPositions;
    private Set<Entity> spawnedEntities;
    
    private int updateTicks;
    
    private int maxSpreadDistance;
    
    public TileEntityTemperatureFill()
    {
        //TODO: This may change, 10 for now
        this.maxSpreadDistance = 10;
        
        //Initialize sets for all strengths
        this.currentTrackedPositions = new Set[this.maxSpreadDistance + 1];
        for (int i = 0; i < this.maxSpreadDistance + 1; i++)
        {
            this.currentTrackedPositions[i] = Sets.newHashSet();
        }
        
        this.queuedTrackedPositions = Sets.newHashSet();
        //TODO: Remove this
        this.spawnedEntities = Sets.newHashSet();
    }
    
    @Override
    public void update() 
    {
        World world = this.getWorld();

        //Verify every 2 seconds
        /*if (++updateTicks % 20 == 0)
        {
            // Ensure there has been no changes since last time
            if (!verify())
            {
                //Refill again
                fill();
            }
        }*/
        
        //TODO: Testing only, Remove this
        //Set all tracked positions to glass, only do this when filled
        

    }

    public void reset()
    {
        //TODO: Remove this
        for (Entity entity : this.spawnedEntities)
        {
            entity.setDead();
        }
        this.spawnedEntities.clear();
        
        //Clear set of current positions
        for (Set<BlockPos> set : this.currentTrackedPositions)
        {
            set.clear();
        }
        this.queuedTrackedPositions.clear();
    }
    
    public void fill()
    {
        reset();
        
        //Add blocks around the temperature modifier block to the queue
        for (EnumFacing facing : EnumFacing.values())
        {
            BlockPos offsetPos = pos.offset(facing);
            
            //Only attempt to update tracking for this position if there is air here.
            //Even positions already being tracked should be filled with air.
            if (this.getWorld().isAirBlock(offsetPos))
                this.queuedTrackedPositions.add(offsetPos);
        }
        
        runStage(this.maxSpreadDistance);
        
        //TODO: Remove this
        for (Set<BlockPos> trackedPositions : this.currentTrackedPositions)
        {
            for (BlockPos trackedPosition : Lists.newArrayList(trackedPositions))
            {
                if (trackedPosition != null)
                {
                    BlockPos pos = trackedPosition;
                    
                    EntitySmallFireball fireball = new EntitySmallFireball(getWorld(), (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
                    this.spawnedEntities.add(fireball);
                    this.getWorld().spawnEntityInWorld(fireball);
                }
            }
        }
    }
    
    private void runStage(int strength)
    {
        //Add queued to current, clear queue
        this.currentTrackedPositions[strength].addAll(this.queuedTrackedPositions);
        this.queuedTrackedPositions.clear();
        
        //Don't spread if strength is 0 (or somehow less)
        if (strength > 0)
        {
            //Populate queue for next stage
            for (BlockPos trackedPosition : this.currentTrackedPositions[strength])
            {      
                BlockPos pos = trackedPosition;
                spreadAroundPos(pos, strength);
            }
            
            //Next stage should have less strength than this
            runStage(strength - 1);
        }
    }
    
    /** Begins tracking this position or updates its strength. Returns true if changed from before **/
    private void setTrackedStrength(BlockPos pos, int strength)
    {
        //Only attempt to update tracking for this position if there is air here.
        //Even positions already being tracked should be filled with air.
        if (!(this.getWorld().isAirBlock(pos))) return;
        
        this.queuedTrackedPositions.add(pos);
    }
    
    /**Strength is the strength of the initial pos, 
     * not what it will spread to its surroundings*/
    private void spreadAroundPos(BlockPos pos, int strength)
    {
        for (EnumFacing facing : EnumFacing.values())
        {
            BlockPos offsetPos = pos.offset(facing);

            //Don't set if the tracked positions already contains this position
            if (this.currentTrackedPositions[strength].contains(offsetPos))
            {
                continue;
            }
            
            //Set suitable adjacent positions as tracked
            setTrackedStrength(offsetPos, strength - 1);
        }
    }

    /** Returns true if verified, false if regen is required */
    /*public boolean verify()
    {
        for (TrackedPosition trackedPosition : this.trackedPositions)
        {
            if (trackedPosition != null)
            {
                UpdateType updateType = trackedPosition.checkForUpdate();

                switch (updateType)
                {
                case REMOVE:
                    trackedPositions[getIndexForPos(trackedPosition.pos)] = null;
                    break;

                case REGEN:
                    return false;

                default:
                    continue;
                }
            }
        }

        return true;
    }*/
    
    private class TrackedPosition
    {
        public final BlockPos pos;
        
        public TrackedPosition(BlockPos pos)
        {
            this.pos = pos;
            
            //Only check adjacents if this position can spread (i.e. its strength is > 0)
            /*if (!this.end)
            {
                for (EnumFacing facing : EnumFacing.values())
                {
                    adjacentStates[facing.ordinal()] = TileEntityTemperatureFill.this.getWorld().getBlockState(pos.offset(facing));
                }
            }*/
        }
        
        /*public UpdateType checkForUpdate()
        {
            World world = TileEntityTemperatureFill.this.getWorld();

            //Tracked positions should only ever be associated with air
            if (!(world.isAirBlock(pos) || world.getBlockState(pos) == Blocks.glass.getDefaultState()))
            {
                //If this position is the end, remove it from tracking. If not, a full regen is required
                return end ? UpdateType.REMOVE : UpdateType.REGEN;
            }
            
            //End positions don't care about their neighbours
            if (!end)
            {
                for (EnumFacing facing : EnumFacing.values())
                {
                    IBlockState state = world.getBlockState(this.pos.offset(facing));
                    
                    if (state != adjacentStates[facing.ordinal()])
                    {
                        return UpdateType.REGEN;
                    }
                }
            }
            
            return UpdateType.NONE;
        }*/
    }
    
    private static enum UpdateType
    {
        NONE, REMOVE, REGEN;
    }
}
