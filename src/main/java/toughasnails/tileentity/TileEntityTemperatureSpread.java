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

//TODO: Don't spread unless the sky can't be seen
public class TileEntityTemperatureSpread extends TileEntity implements ITickable
{
    private Set<BlockPos>[] filledPositions;
    private Set<BlockPos> obstructedPositions;
    
    private int updateTicks;
    private int maxSpreadDistance;
    
    private static final boolean ENABLE_DEBUG = true;
    
    private Set<Entity> spawnedEntities;
    
    public TileEntityTemperatureSpread()
    {
        this.maxSpreadDistance = 50;
        
        //Initialize sets for all strengths
        this.filledPositions = new Set[this.maxSpreadDistance + 1];
        for (int i = 0; i < this.maxSpreadDistance + 1; i++)
        {
            this.filledPositions[i] = Sets.newConcurrentHashSet();
        }
        this.obstructedPositions = Sets.newConcurrentHashSet();
        
        if (ENABLE_DEBUG) this.spawnedEntities = Sets.newHashSet();
    }
    
    //TODO: Stagger updates if necessary, so verification occurs slower away from the base position
    //Doesn't really seem necessary at the moment, it appears to be fast enough
    @Override
    public void update() 
    {
        World world = this.getWorld();

        //Verify every 2 seconds
        if (++updateTicks % 20 == 0)
        {
            // Ensure there has been no changes since last time
            if (!verify())
            {
                //Refill again
                fill();
            }
        }
    }

    public void reset()
    {
        if (ENABLE_DEBUG)
        {
            for (Entity entity : this.spawnedEntities)
            {
                entity.setDead();
            }
            this.spawnedEntities.clear();
        }
        
        //Clear set of current positions
        for (Set<BlockPos> set : this.filledPositions)
        {
            set.clear();
        }
        this.obstructedPositions.clear();
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
                this.filledPositions[this.maxSpreadDistance].add(offsetPos);
        }
        
        runStage(this.maxSpreadDistance - 1);
        
        if (ENABLE_DEBUG)
        {
            for (Set<BlockPos> trackedPositions : this.filledPositions)
            {
                for (BlockPos trackedPosition : trackedPositions)
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
    }
    
    private void runStage(int strength)
    {
        //Don't spread if strength is 0 (or somehow less)
        if (strength > 0)
        {
            //Populate queue for next stage
            for (BlockPos trackedPosition : this.filledPositions[strength + 1])
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
        if (this.getWorld().isAirBlock(pos))
        {
            this.filledPositions[strength].add(pos);
        }
        else
        {
            this.obstructedPositions.add(pos);
        }
    }
    
    /**Strength is the strength of the initial pos, 
     * not what it will spread to its surroundings*/
    private void spreadAroundPos(BlockPos pos, int strength)
    {
        for (EnumFacing facing : EnumFacing.values())
        {
            BlockPos offsetPos = pos.offset(facing);

            //Don't set if the tracked positions already contains this position
            if (this.filledPositions[strength + 1].contains(offsetPos))
            {
                continue;
            }
            
            //Set suitable adjacent positions as tracked
            setTrackedStrength(offsetPos, strength);
        }
    }

    /** Returns true if verified, false if regen is required */
    public boolean verify()
    {
        for (Set<BlockPos> trackedPositions : this.filledPositions)
        {
            for (BlockPos pos : trackedPositions)
            {
                if (!this.getWorld().isAirBlock(pos)) return false;
            }
        }
        
        for (BlockPos pos : this.obstructedPositions)
        {
            if (this.getWorld().isAirBlock(pos)) return false;
        }

        return true;
    }
}
